package juuxel.adorn.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PaneBlock;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public final class ChainLinkFenceBlock extends PaneBlock implements BlockWithDescription {
    public static final BooleanProperty UP = Properties.UP;
    public static final BooleanProperty DOWN = Properties.DOWN;

    public ChainLinkFenceBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(UP, false).with(DOWN, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(UP, DOWN);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        var state = super.getPlacementState(ctx);
        var world = ctx.getWorld();
        var pos = ctx.getBlockPos();

        return state
            .with(UP, connectsVerticallyTo(world.getBlockState(pos.up())))
            .with(DOWN, connectsVerticallyTo(world.getBlockState(pos.down())));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        var result = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);

        if (direction == Direction.UP) {
            result = result.with(UP, connectsVerticallyTo(neighborState));
        } else if (direction == Direction.DOWN) {
            result = result.with(DOWN, connectsVerticallyTo(neighborState));
        }

        return result;
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        return false;
    }

    private static boolean connectsVerticallyTo(BlockState state) {
        return state.getBlock() instanceof ChainLinkFenceBlock;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public boolean connectsTo(BlockState state, boolean sideSolidFullSquare) {
        return super.connectsTo(state, sideSolidFullSquare) || state.isIn(BlockTags.FENCE_GATES);
    }
}
