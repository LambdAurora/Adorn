package juuxel.adorn.util;

import net.minecraft.block.Block;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Shapes {
    /**
     * Creates a map of horizontal cuboid VoxelShape rotations from the provided coordinates for <strong>east</strong>.
     *
     * <p>The coordinates are specified like in a model json or {@code createCuboidShape}:
     * {@code { from: ([x0], [y0], [z0]), to: ([x1], [y1], [z1]) }}
     * 
     * @deprecated Use north instead: {@code buildShapeRotationsFromNorth(z0, y0, 16 - x1, z1, y1, 16 - x0)}
     */
    @Deprecated
    public static Map<Direction, VoxelShape> buildShapeRotations(int x0, int y0, int z0, int x1, int y1, int z1) {
        return buildShapeRotationsFromNorth(z0, y0, 16 - x1, z1, y1, 16 - x0);
    }

    /**
     * Creates a map of horizontal cuboid VoxelShape rotations from the provided coordinates for <strong>north</strong>.
     *
     * <p>The coordinates are specified like in a model json or {@code createCuboidShape}:
     * {@code { from: ([x0], [y0], [z0]), to: ([x1], [y1], [z1]) }}
     */
    public static Map<Direction, VoxelShape> buildShapeRotationsFromNorth(int x0, int y0, int z0, int x1, int y1, int z1) {
        Map<Direction, VoxelShape> result = new EnumMap<>(Direction.class);
        result.put(Direction.NORTH, Block.createCuboidShape(x0, y0, z0, x1, y1, z1));
        result.put(Direction.SOUTH, Block.createCuboidShape(16.0 - x1, y0, 16.0 - z1, 16.0 - x0, y1, 16.0 - z0));
        result.put(Direction.EAST, Block.createCuboidShape(16.0 - z1, y0, x0, 16.0 - z0, y1, x1));
        result.put(Direction.WEST, Block.createCuboidShape(z0, y0, 16.0 - x1, z1, y1, 16.0 - x0));
        return result;
    }

    /**
     * Merges the shape maps together.
     */
    @SafeVarargs
    public static Map<Direction, VoxelShape> mergeShapeMaps(Map<Direction, VoxelShape>... maps) {
        return new EnumMap<>(
            Direction.Type.HORIZONTAL.stream().collect(Collectors.toMap(
                Function.identity(),
                direction -> Arrays.stream(maps)
                    .map(map -> {
                        var shape = map.get(direction);
                        if (shape == null) throw new IllegalArgumentException("Map is missing shape for " + direction);
                        return shape;
                    })
                    .reduce(VoxelShapes::union)
                    .orElseThrow()
            ))
        );
    }

    /**
     * Merges the shape into the shape map.
     */
    public static Map<Direction, VoxelShape> mergeIntoShapeMap(Map<Direction, VoxelShape> map, VoxelShape shape) {
        var result = new EnumMap<Direction, VoxelShape>(Direction.class);
        map.forEach((direction, existing) -> result.put(direction, VoxelShapes.union(existing, shape)));
        return result;
    }
}
