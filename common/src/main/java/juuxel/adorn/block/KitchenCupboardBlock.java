package juuxel.adorn.block;

import juuxel.adorn.block.entity.KitchenCupboardBlockEntity;
import juuxel.adorn.block.entity.SimpleContainerBlockEntity;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.lib.AdornStats;
import juuxel.adorn.platform.PlatformBridges;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.menu.Menu;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public final class KitchenCupboardBlock extends AbstractKitchenCounterBlock implements BlockEntityProvider, BlockWithDescription {
    private static final String DESCRIPTION_KEY = "block.adorn.kitchen_cupboard.description";

    public KitchenCupboardBlock(BlockVariant variant) {
        super(variant);
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;

        if (world.getBlockEntity(pos) instanceof KitchenCupboardBlockEntity cupboard) {
            PlatformBridges.get().getMenus().open(player, cupboard, pos);
            player.incrementStat(AdornStats.OPEN_KITCHEN_CUPBOARD);
        }

        return ActionResult.CONSUME;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        ItemScatterer.onStateReplaced(state, newState, world, pos);
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomName() && world.getBlockEntity(pos) instanceof LootableContainerBlockEntity container) {
            container.setCustomName(stack.getName());
        }
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return Menu.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return AdornBlockEntities.KITCHEN_CUPBOARD.get().instantiate(pos, state);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBlockEntity(pos) instanceof SimpleContainerBlockEntity container) {
            container.onScheduledTick();
        }
    }
}
