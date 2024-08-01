package juuxel.adorn.item;

import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.fluid.StepMaximum;
import juuxel.adorn.lib.AdornSounds;
import juuxel.adorn.platform.FluidBridge;
import juuxel.adorn.util.ColorsKt;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.FluidDrainable;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class WateringCanItem extends ItemWithDescription {
    private static final String NBT_WATER_LEVEL = "WaterLevel";
    public static final String NBT_FERTILIZER_LEVEL = "FertilizerLevel";
    private static final int ITEM_BAR_STEPS = 13;
    private static final int MAX_WATER_LEVEL = 50;
    public static final int MAX_FERTILIZER_LEVEL = 32;
    private static final float WATER_LEVEL_DIVISOR = 1f / MAX_WATER_LEVEL;
    private static final int WATER_LEVELS_PER_BUCKET = 10;

    private static final StepMaximum FLUID_DRAIN_PREDICATE = new StepMaximum(0L, 1000L, 1000L / WATER_LEVELS_PER_BUCKET, FluidUnit.LITRE);

    public WateringCanItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var stack = user.getStackInHand(hand);
        var success = false;

        var hitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return TypedActionResult.pass(stack);
        }

        var nbt = stack.getOrCreateNbt();
        var waterLevel = nbt.getInt(NBT_WATER_LEVEL);
        var pos = hitResult.getBlockPos();
        var state = world.getBlockState(pos);
        var block = state.getBlock();

        if (waterLevel < MAX_WATER_LEVEL) {
            // Check for drainable water

            // Note: we have a water check because we can't revert changes for non-water fluid sources
            if (block instanceof FluidDrainable drainable && world.getFluidState(pos).isOf(Fluids.WATER)) {
                var drained = drainable.tryDrainFluid(user, world, pos, state);
                drainable.getBucketFillSound().ifPresent(sound ->user.playSound(sound, 1f, 1f));

                if (drained.isOf(Items.WATER_BUCKET)) {
                    waterLevel = Math.min(waterLevel + WATER_LEVELS_PER_BUCKET, MAX_WATER_LEVEL);
                    nbt.putInt(NBT_WATER_LEVEL, waterLevel);
                    success = true;
                }
            } else {
                var drained = FluidBridge.get().drain(world, pos, null, hitResult.getSide().getOpposite(), Fluids.WATER, FLUID_DRAIN_PREDICATE);

                if (drained != null) {
                    long amount = FluidUnit.convert(drained.getAmount(), drained.getUnit(), FluidUnit.LITRE);
                    int levels = (int) (amount / FLUID_DRAIN_PREDICATE.getStep());
                    waterLevel = Math.min(waterLevel + levels, MAX_WATER_LEVEL);
                    nbt.putInt(NBT_WATER_LEVEL, waterLevel);
                    success = true;
                    user.playSound(SoundEvents.ITEM_BUCKET_FILL, 1f, 1f);
                }
            }
        }

        if (!success && waterLevel > 0) {
            success = true;

            waterLevel--;
            nbt.putInt(NBT_WATER_LEVEL, waterLevel);
            world.emitGameEvent(user, GameEvent.ITEM_INTERACT_FINISH, pos);
            world.playSound(user, pos, AdornSounds.ITEM_WATERING_CAN_WATER.get(), SoundCategory.PLAYERS);
            user.getItemCooldownManager().set(this, 10);

            var mut = new BlockPos.Mutable();
            for (int xo = -1; xo <= 1; xo++) {
                for (int zo = -1; zo <= 1; zo++) {
                    mut.set(pos.getX() + xo, pos.getY(), pos.getZ() + zo);
                    water(world, mut, user, stack);

                    if (world instanceof ServerWorld serverWorld) {
                        spawnParticlesAt(serverWorld, mut, hitResult.getPos().y);
                    }
                }
            }
        }

        return success ? TypedActionResult.success(stack, world.isClient()) : TypedActionResult.pass(stack);
    }

    private void water(World world, BlockPos pos, PlayerEntity player, ItemStack stack) {
        var nbt = stack.getOrCreateNbt();
        var fertilizerLevel = nbt.getInt(NBT_FERTILIZER_LEVEL);
        var state = world.getBlockState(pos);
        var block = state.getBlock();

        if (fertilizerLevel > 0 && world.random.nextInt(9) == 0) {
            if (block instanceof Fertilizable fertilizable && fertilizable.isFertilizable(world, pos, state)) {
                if (world instanceof ServerWorld serverWorld && fertilizable.canGrow(world, world.random, pos, state)) {
                    fertilizable.grow(serverWorld, world.random, pos, state);
                }

                world.syncWorldEvent(player, WorldEvents.BONE_MEAL_USED, pos, 5);
            }

            nbt.putInt(NBT_FERTILIZER_LEVEL, fertilizerLevel - 1);
        }

        if (!world.isClient) {
            if (block instanceof FarmlandBlock) {
                waterFarmlandBlock(world, pos, state);
            } else if (!state.isFullCube(world, pos)) { // We can't water through full cubes
                var downPos = pos.down();
                var downState = world.getBlockState(downPos);

                if (downState.getBlock() instanceof FarmlandBlock) {
                    waterFarmlandBlock(world, downPos, downState);
                }
            }
        }
    }

    private void waterFarmlandBlock(World world, BlockPos pos, BlockState state) {
        var moisture = state.get(FarmlandBlock.MOISTURE);

        if (moisture < FarmlandBlock.MAX_MOISTURE) {
            var moistureChange = world.random.nextBetween(2, 6);
            var newMoisture = Math.min(moisture + moistureChange, FarmlandBlock.MAX_MOISTURE);
            world.setBlockState(pos, state.with(FarmlandBlock.MOISTURE, newMoisture), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        if (stack.hasNbt()) {
            var nbt = stack.getNbt();
            if (nbt.contains(NBT_WATER_LEVEL, NbtElement.INT_TYPE)) {
                var waterLevel = MathHelper.clamp(nbt.getInt(NBT_WATER_LEVEL), 0, MAX_WATER_LEVEL);
                return MathHelper.lerp(WATER_LEVEL_DIVISOR * waterLevel, 0, ITEM_BAR_STEPS);
            }
        }

        return 0;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        var nbt = stack.getOrCreateNbt();
        var rg = MathHelper.clampedMap(
            nbt.getInt(NBT_FERTILIZER_LEVEL),
            // From:
            0f, MAX_FERTILIZER_LEVEL,
            // To:
            0.4f, 1f
        );
        return ColorsKt.color(rg, rg, 1f);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        var nbt = stack.getNbt();
        var currentLevel = Text.literal(nbt != null ? Integer.toString(nbt.getInt(NBT_FERTILIZER_LEVEL)) : "0").formatted(Formatting.DARK_AQUA);
        var maxLevel = Text.literal(Integer.toString(MAX_FERTILIZER_LEVEL)).formatted(Formatting.DARK_AQUA);
        tooltip.add(Text.translatable("item.adorn.watering_can.fertilizer", currentLevel, maxLevel).formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }

    private static void spawnParticlesAt(ServerWorld world, BlockPos pos, double y) {
        double px = pos.getX() + 0.3 + world.random.nextDouble() * 0.4;
        double py = y + 0.1;
        double pz = pos.getZ() + 0.3 + world.random.nextDouble() * 0.4;
        double vx = world.random.nextDouble() * 0.2 - 0.1;
        double vy = 0.1;
        double vz = world.random.nextDouble() * 0.2 - 0.1;
        world.spawnParticles(ParticleTypes.SPLASH, px, py, pz, 4, vx, vy, vz, 0.5);
    }
}
