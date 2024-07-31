package juuxel.adorn.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.stream.IntStream;

public record Vec2i(int x, int y) {
    public static final Codec<Vec2i> CODEC = new Codec<>() {
        @Override
        public <T> DataResult<T> encode(Vec2i input, DynamicOps<T> ops, T prefix) {
            return ops.mergeToPrimitive(prefix, ops.createIntList(IntStream.of(input.x, input.y)));
        }

        @Override
        public <T> DataResult<Pair<Vec2i, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getIntStream(input).flatMap(stream -> {
                var iter = stream.iterator();

                if (!iter.hasNext()) return mismatchedComponentCountResult();
                int x = iter.nextInt();
                if (!iter.hasNext()) return mismatchedComponentCountResult();
                int y = iter.nextInt();
                if (iter.hasNext()) return mismatchedComponentCountResult();

                return DataResult.success(Pair.of(new Vec2i(x, y), ops.empty()));
            });
        }

        @Override
        public String toString() {
            return "Vec2i";
        }
    };

    private static <T> DataResult<T> mismatchedComponentCountResult() {
        return DataResult.error(() -> "Vec2i must have exactly two int components");
    }
}
