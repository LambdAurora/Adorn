package juuxel.adorn.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public final class NbtUtil {
    private static final Logger LOGGER = Logging.logger();

    public static <T> @Nullable T getWithCodec(NbtCompound nbt, String name, Codec<T> codec, RegistryWrapper.WrapperLookup registries) {
        var ops = registries.getOps(NbtOps.INSTANCE);
        var subNbt = nbt.get(name);
        if (subNbt == null) return null;
        return switch (codec.parse(ops, subNbt)) {
            case DataResult.Success(T value, Lifecycle lifecycle) -> value;
            case DataResult.Error<T> error -> {
                LOGGER.error("[Adorn] Could not deserialize {}: {}", name, error.message());
                yield null;
            }
        };
    }

    public static <T> void putWithCodec(NbtCompound nbt, String name, Codec<T> codec, T value, RegistryWrapper.WrapperLookup registries) {
        var ops = registries.getOps(NbtOps.INSTANCE);
        var encoded = codec.encodeStart(ops, value);

        switch (encoded) {
            case DataResult.Success(NbtElement subNbt, Lifecycle lifecycle) -> nbt.put(name, subNbt);
            case DataResult.Error<NbtElement> error -> LOGGER.error("[Adorn] Could not serialize {}: {}", name, error.message());
        }
    }

    public static void putText(NbtCompound nbt, String name, Text text, RegistryWrapper.WrapperLookup registries) {
        putWithCodec(nbt, name, TextCodecs.CODEC, text, registries);
    }

    public static @Nullable Text getText(NbtCompound nbt, String name, RegistryWrapper.WrapperLookup registries) {
        return getWithCodec(nbt, name, TextCodecs.CODEC, registries);
    }

    public static BlockPos getBlockPos(NbtCompound nbt, String key) {
        int[] coords = nbt.getIntArray(key);
        return new BlockPos(coords[0], coords[1], coords[2]);
    }

    public static void putBlockPos(NbtCompound nbt, String key, BlockPos pos) {
        nbt.putIntArray(key, new int[] { pos.getX(), pos.getY(), pos.getZ() });
    }
}
