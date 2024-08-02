package juuxel.adorn.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.text.Text;

public final class MoreCodecs {
    public static final Codec<Text> TEXT = new Codec<>() {
        @Override
        public <T> DataResult<T> encode(Text input, DynamicOps<T> ops, T prefix) {
            var json = Text.Serialization.toJsonTree(input);
            var converted = Dynamic.convert(JsonOps.INSTANCE, ops, json);
            return ops.mergeToPrimitive(prefix, converted);
        }

        @Override
        public <T> DataResult<Pair<Text, T>> decode(DynamicOps<T> ops, T input) {
            var json = Dynamic.convert(ops, JsonOps.INSTANCE, input);
            var text = Text.Serialization.fromJsonTree(json);
            return text != null ? DataResult.success(Pair.of(text, ops.empty())) : DataResult.error(() -> "Could not decode text " + json);
        }

        @Override
        public String toString() {
            return "Text";
        }
    };
}
