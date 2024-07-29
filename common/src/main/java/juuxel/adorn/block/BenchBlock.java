package juuxel.adorn.block;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectArrayMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.lib.AdornStats;
import juuxel.adorn.util.ExtensionsKt;
import juuxel.adorn.util.Shapes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.List;

public final class BenchBlock extends SeatBlock implements Waterloggable, BlockWithDescription {
    public static final EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;
    public static final BooleanProperty CONNECTED_N = BooleanProperty.of("connected_n");
    public static final BooleanProperty CONNECTED_P = BooleanProperty.of("connected_p");
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final String DESCRIPTION_KEY = "block.adorn.bench.description";
    private static final VoxelShape X_TOP_SHAPE = createCuboidShape(0.0, 8.0, 1.0, 16.0, 10.0, 15.0);
    private static final VoxelShape Z_TOP_SHAPE = createCuboidShape(1.0, 8.0, 0.0, 15.0, 10.0, 16.0);
    private static final Byte2ObjectMap<VoxelShape> SHAPES = new Byte2ObjectArrayMap<>();

    static {
        var legShapes = Shapes.buildShapeRotationsFromNorth(2, 0, 2, 14, 8, 4);
        var booleans = new boolean[] { true, false };

        for (var axis : new Direction.Axis[] { Direction.Axis.X, Direction.Axis.Z }) {
            var topShape = axis == Direction.Axis.X ? X_TOP_SHAPE : Z_TOP_SHAPE;
            var negativeLeg = legShapes.get(ExtensionsKt.getDirection(axis, Direction.AxisDirection.NEGATIVE));
            var positiveLeg = legShapes.get(ExtensionsKt.getDirection(axis, Direction.AxisDirection.POSITIVE));

            for (var connectedN : booleans) {
                for (var connectedP : booleans) {
                    List<VoxelShape> parts = new ArrayList<>();

                    if (!connectedN) {
                        parts.add(negativeLeg);
                    }

                    if (!connectedP) {
                        parts.add(positiveLeg);
                    }

                    var key = Bits.buildBenchState(axis, connectedN, connectedP);
                    SHAPES.put(key, VoxelShapes.union(topShape, parts.toArray(VoxelShape[]::new)));
                }
            }
        }
    }

    public BenchBlock(BlockVariant variant) {
        super(variant.createSettings());
        setDefaultState(getDefaultState()
            .with(AXIS, Direction.Axis.Z)
            .with(CONNECTED_N, false)
            .with(CONNECTED_P, false)
            .with(WATERLOGGED, false));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        var state = getDefaultState()
            .with(AXIS, ExtensionsKt.turnHorizontally(ctx.getHorizontalPlayerFacing().getAxis()))
            .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
        return updateConnections(ctx.getWorld(), ctx.getBlockPos(), state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return updateConnections(world, pos, state);
    }

    private BlockState updateConnections(BlockView world, BlockPos pos, BlockState state) {
        for (var axisDirection : Direction.AxisDirection.values()) {
            var property = axisDirection == Direction.AxisDirection.NEGATIVE ? CONNECTED_N : CONNECTED_P;
            var neighbor = world.getBlockState(pos.offset(state.get(AXIS), axisDirection.offset()));
            var connected = neighbor.getBlock() instanceof BenchBlock && neighbor.get(AXIS) == state.get(AXIS);
            state = state.with(property, connected);
        }
        return state;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(Bits.buildBenchState(state.get(AXIS), state.get(CONNECTED_N), state.get(CONNECTED_P)));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(AXIS, CONNECTED_N, CONNECTED_P, WATERLOGGED);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return switch (rotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                yield state.with(AXIS, ExtensionsKt.turnHorizontally(state.get(AXIS)));
            default:
                yield state;
        };
    }

    @Override
    public Identifier getSittingStat() {
        return AdornStats.SIT_ON_BENCH;
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }
}
