package juuxel.adorn.block;

import juuxel.adorn.lib.AdornTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public abstract class AbstractChimneyBlock extends Block implements Waterloggable {
    public static final BooleanProperty CONNECTED = BooleanProperty.of("connected");
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private static final VoxelShape TOP_SHAPE = createCuboidShape(4.0, 0.0, 4.0, 12.0, 12.0, 12.0);
    private static final VoxelShape MIDDLE_SHAPE = createCuboidShape(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);

    public AbstractChimneyBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
            .with(CONNECTED, false)
            .with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(CONNECTED, WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return updateConnections(
            getDefaultState().with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER),
            ctx.getWorld().getBlockState(ctx.getBlockPos().up())
        );
    }

    private BlockState updateConnections(BlockState state, BlockState neighborState) {
        return state.with(CONNECTED, neighborState.isIn(AdornTags.CHIMNEYS.block()));
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (state.get(WATERLOGGED)) {
            tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return direction == Direction.UP ? updateConnections(state, neighborState) : state;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(CONNECTED) ? MIDDLE_SHAPE : TOP_SHAPE;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    public static Settings createBlockSettings(MapColor color) {
        return createBlockSettings(color, 2f);
    }

    public static Settings createBlockSettings(MapColor color, float hardness) {
        return Settings.create()
            .mapColor(color)
            .solid()
            .requiresTool()
            .strength(hardness, 6f)
            .ticksRandomly()
            .nonOpaque();
    }
}
