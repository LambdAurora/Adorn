package juuxel.adorn.block;

import juuxel.adorn.block.variant.BlockVariant;
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
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public final class CoffeeTableBlock extends Block implements Waterloggable, BlockWithDescription {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private static final VoxelShape SHAPE = VoxelShapes.union(
        createCuboidShape(0.0, 4.0, 0.0, 16.0, 6.0, 16.0),
        createCuboidShape(0.0, 10.0, 0.0, 16.0, 12.0, 16.0),
        createCuboidShape(0.0, 0.0, 0.0, 2.0, 10.0, 2.0),
        createCuboidShape(14.0, 0.0, 0.0, 16.0, 10.0, 2.0),
        createCuboidShape(14.0, 0.0, 14.0, 16.0, 10.0, 16.0),
        createCuboidShape(0.0, 0.0, 14.0, 2.0, 10.0, 16.0)
    );
    private static final String DESCRIPTION_KEY = "block.adorn.coffee_table.description";

    public CoffeeTableBlock(BlockVariant variant) {
        super(variant.createSettings().nonOpaque());
        setDefaultState(getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
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
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
}
