package juuxel.adorn.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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

public abstract class AbstractTableBlock extends CarpetedBlock implements Waterloggable {
    public static final BooleanProperty NORTH = Properties.NORTH;
    public static final BooleanProperty EAST = Properties.EAST;
    public static final BooleanProperty SOUTH = Properties.SOUTH;
    public static final BooleanProperty WEST = Properties.WEST;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public AbstractTableBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(WATERLOGGED, false));
    }

    protected abstract boolean canConnectTo(BlockState state, Direction sideOfSelf);

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(NORTH, EAST, SOUTH, WEST, WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return updateConnections(
            super.getPlacementState(ctx)
                .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER),
            ctx.getWorld(),
            ctx.getBlockPos()
        );
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (state.get(WATERLOGGED)) {
            tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return updateConnections(super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random), world, pos);
    }

    private BlockState updateConnections(BlockState state, BlockView world, BlockPos pos) {
        return state.with(NORTH, canConnectTo(world.getBlockState(pos.offset(Direction.NORTH)), Direction.NORTH))
            .with(EAST, canConnectTo(world.getBlockState(pos.offset(Direction.EAST)), Direction.EAST))
            .with(SOUTH, canConnectTo(world.getBlockState(pos.offset(Direction.SOUTH)), Direction.SOUTH))
            .with(WEST, canConnectTo(world.getBlockState(pos.offset(Direction.WEST)), Direction.WEST));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShapeForKey(
            getShapeKey(
                state.get(NORTH), state.get(EAST), state.get(SOUTH), state.get(WEST),
                isCarpetingEnabled() && state.get(CARPET).isPresent()
            )
        );
    }

    protected static int getShapeKey(boolean north, boolean east, boolean south, boolean west, boolean hasCarpet) {
        return (north ? 1 : 0) << 4 | (east ? 1 : 0) << 3 | (south ? 1 : 0) << 2 | (west ? 1 : 0) << 1 | (hasCarpet ? 1 : 0);
    }

    protected abstract VoxelShape getShapeForKey(int key);

    @Override
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }
}
