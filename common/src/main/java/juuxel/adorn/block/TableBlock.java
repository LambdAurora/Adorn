package juuxel.adorn.block;

import juuxel.adorn.block.variant.BlockVariant;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class TableBlock extends AbstractTableBlock implements BlockWithDescription {
    private static final String DESCRIPTION_KEY = "block.adorn.table.description";
    private static final VoxelShape[] SHAPES = new VoxelShape[32];

    static {
        var topShape = createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0);
        var legX0Z0 = createCuboidShape(1.0, 0.0, 1.0, 4.0, 14.0, 4.0);
        var legX1Z0 = createCuboidShape(12.0, 0.0, 1.0, 15.0, 14.0, 4.0);
        var legX0Z1 = createCuboidShape(1.0, 0.0, 12.0, 4.0, 14.0, 15.0);
        var legX1Z1 = createCuboidShape(12.0, 0.0, 12.0, 15.0, 14.0, 15.0);
        var booleans = new boolean[] { true, false };

        for (var north : booleans) {
            for (var east : booleans) {
                for (var south : booleans) {
                    for (var west : booleans) {
                        for (var hasCarpet : booleans) {
                            var key = getShapeKey(north, east, south, west, hasCarpet);
                            var shape = makeShape(north, east, south, west, hasCarpet, topShape, legX0Z0, legX1Z0, legX0Z1, legX1Z1);
                            SHAPES[key] = shape;
                        }
                    }
                }
            }
        }
    }

    public TableBlock(BlockVariant variant) {
        super(createSettings(variant));
    }

    private static Settings createSettings(BlockVariant variant) {
        return variant.createSettings().solid();
    }

    @Override
    public @Nullable Identifier getSittingStat() {
        return null;
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    protected boolean isSittingEnabled() {
        return false;
    }

    @Override
    protected boolean canConnectTo(BlockState state, Direction sideOfSelf) {
        return state.getBlock() instanceof TableBlock;
    }

    @Override
    protected VoxelShape getShapeForKey(int key) {
        return SHAPES[key];
    }

    private static VoxelShape makeShape(
        boolean north, boolean east, boolean south, boolean west, boolean hasCarpet,
        VoxelShape topShape, VoxelShape legX0Z0, VoxelShape legX1Z0, VoxelShape legX0Z1, VoxelShape legX1Z1
    ) {
        List<VoxelShape> parts = new ArrayList<>();
        parts.add(topShape);

        if (north || east || south || west) {
            var trueCount = 0;
            if (north) trueCount++;
            if (east) trueCount++;
            if (south) trueCount++;
            if (west) trueCount++;

            if (trueCount == 2) {
                // Corners
                if (north && west) {
                    parts.add(legX1Z1);
                } else if (north && east) {
                    parts.add(legX0Z1);
                } else if (south && west) {
                    parts.add(legX1Z0);
                } else if (south && east) {
                    parts.add(legX0Z0);
                }
            } else if (trueCount == 1) {
                // Ends
                if (north) {
                    parts.add(legX0Z1);
                    parts.add(legX1Z1);
                } else if (south) {
                    parts.add(legX0Z0);
                    parts.add(legX1Z0);
                } else if (east) {
                    parts.add(legX0Z0);
                    parts.add(legX0Z1);
                } else {
                    parts.add(legX1Z0);
                    parts.add(legX1Z1);
                }
            }
        } else {
            // No connections = all legs
            parts.add(legX0Z0);
            parts.add(legX1Z0);
            parts.add(legX0Z1);
            parts.add(legX1Z1);
        }

        if (hasCarpet) {
            parts.add(CARPET_SHAPE);
        }

        return parts.stream().reduce(VoxelShapes::union).get();
    }
}
