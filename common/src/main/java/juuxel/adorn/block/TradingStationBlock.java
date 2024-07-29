package juuxel.adorn.block;

import com.mojang.serialization.MapCodec;
import juuxel.adorn.block.entity.TradingStationBlockEntity;
import juuxel.adorn.criterion.AdornCriteria;
import juuxel.adorn.lib.AdornGameRules;
import juuxel.adorn.lib.AdornStats;
import juuxel.adorn.util.NbtExtensionsKt;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class TradingStationBlock extends VisibleBlockWithEntity implements BlockWithDescription {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private static final String OWNER_DESCRIPTION = "block.adorn.trading_station.description.owner";
    private static final VoxelShape OUTLINE_SHAPE = VoxelShapes.union(
        createCuboidShape(0.0, 11.0, 0.0, 16.0, 16.0, 16.0),
        createLegShape()
    );
    private static final VoxelShape COLLISION_SHAPE = VoxelShapes.union(
        createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0),
        createLegShape()
    );

    public TradingStationBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (placer instanceof PlayerEntity player && world.getBlockEntity(pos) instanceof TradingStationBlockEntity tradingStation) {
            tradingStation.setOwner(player);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;

        if (world.getBlockEntity(pos) instanceof TradingStationBlockEntity be) {
            if (be.getOwner() == null) {
                be.setOwner(player);
            }

            if (!be.isOwner(player)) {
                var handStack = player.getStackInHand(hand);
                var trade = be.getTrade();
                var validPayment = ItemStack.canCombine(handStack, trade.getPrice()) &&
                    handStack.getCount() >= trade.getPrice().getCount();
                var canInsertPayment = be.getStorage().canInsert(trade.getPrice());

                if (trade.isEmpty()) {
                    player.sendMessage(Text.translatable("block.adorn.trading_station.empty_trade"), true);
                } else if (!be.isStorageStocked()) {
                    player.sendMessage(Text.translatable("block.adorn.trading_station.storage_not_stocked"), true);
                } else if (!canInsertPayment) {
                    player.sendMessage(Text.translatable("block.adorn.trading_station.storage_full"), true);
                } else if (validPayment) {
                    handStack.decrement(trade.getPrice().getCount());
                    var soldItem = trade.getSelling().copy();
                    player.giveItemStack(soldItem);
                    be.getStorage().tryExtract(trade.getSelling());
                    be.getStorage().tryInsert(trade.getPrice());
                    player.incrementStat(AdornStats.INTERACT_WITH_TRADING_STATION);

                    if (player instanceof ServerPlayerEntity serverPlayer) {
                        AdornCriteria.BOUGHT_FROM_TRADING_STATION.get().trigger(serverPlayer, soldItem);
                    }
                }
            } else {
                player.openMenu(be);
                player.incrementStat(AdornStats.INTERACT_WITH_TRADING_STATION);
            }
        }

        return ActionResult.CONSUME;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock()) && world.getBlockEntity(pos) instanceof TradingStationBlockEntity tradingStation) {
            if (!world.getGameRules().getBoolean(AdornGameRules.DROP_LOCKED_TRADING_STATIONS)) {
                ItemScatterer.spawn(world, pos, tradingStation.getStorage());
            }

            world.updateComparators(pos, this);
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return AdornBlockEntities.TRADING_STATION.get().instantiate(pos, state);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);

        var nbt = stack.getSubNbt(BlockItem.BLOCK_ENTITY_TAG_KEY);
        if (nbt != null && nbt.contains(TradingStationBlockEntity.NBT_TRADING_OWNER)) {
            var owner = NbtExtensionsKt.getText(nbt, TradingStationBlockEntity.NBT_TRADING_OWNER_NAME);
            if (owner == null) owner = TradingStationBlockEntity.UNKNOWN_OWNER;
            tooltip.add(Text.translatable(OWNER_DESCRIPTION, owner.copy().formatted(Formatting.WHITE)).formatted(Formatting.GREEN));
        }
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        throw new UnsupportedOperationException();
    }

    private static VoxelShape createLegShape() {
        return VoxelShapes.union(
            createCuboidShape(1.0, 0.0, 1.0, 4.0, 14.0, 4.0),
            createCuboidShape(12.0, 0.0, 1.0, 15.0, 14.0, 4.0),
            createCuboidShape(1.0, 0.0, 12.0, 4.0, 14.0, 15.0),
            createCuboidShape(12.0, 0.0, 12.0, 15.0, 14.0, 15.0)
        );
    }
}
