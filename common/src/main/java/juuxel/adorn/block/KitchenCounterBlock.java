package juuxel.adorn.block;

import juuxel.adorn.block.property.FrontConnection;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public class KitchenCounterBlock extends AbstractKitchenCounterBlock implements BlockWithDescription {
    public static final EnumProperty<FrontConnection> FRONT = EnumProperty.of("front", FrontConnection.class);
    private static final String DESCRIPTION_KEY = "block.adorn.kitchen_counter.description";

    public KitchenCounterBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FRONT, FrontConnection.NONE));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FRONT);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return updateConnections(super.getPlacementState(ctx), ctx.getWorld(), ctx.getBlockPos());
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        return updateConnections(state, world, pos);
    }

    private BlockState updateConnections(BlockState state, BlockView world, BlockPos pos) {
        var facing = state.get(FACING);
        var frontState = world.getBlockState(pos.offset(facing));
        var frontConnection = FrontConnection.NONE;

        if (frontState.getBlock() instanceof AbstractKitchenCounterBlock) {
            var frontFacing = frontState.get(FACING);
            if (frontFacing == facing.rotateYClockwise()) {
                frontConnection = FrontConnection.LEFT;
            } else if (frontFacing == facing.rotateYCounterclockwise()) {
                frontConnection = FrontConnection.RIGHT;
            }
        }

        return state.with(FRONT, frontConnection);
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }
}
