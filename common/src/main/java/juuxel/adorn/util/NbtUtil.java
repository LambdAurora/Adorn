package juuxel.adorn.util;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public final class NbtUtil {
    public static void putText(NbtCompound nbt, String name, Text text) {
        nbt.put(name, Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, Text.Serialization.toJsonTree(text)));
    }

    public static @Nullable Text getText(NbtCompound nbt, String name) {
        var tag = nbt.get(name);
        if (tag == null) return null;

        return Text.Serialization.fromJsonTree(
            Dynamic.convert(
                NbtOps.INSTANCE,
                JsonOps.INSTANCE,
                tag
            )
        );
    }

    public static BlockPos getBlockPos(NbtCompound nbt, String key) {
        int[] coords = nbt.getIntArray(key);
        return new BlockPos(coords[0], coords[1], coords[2]);
    }

    public static void putBlockPos(NbtCompound nbt, String key, BlockPos pos) {
        nbt.putIntArray(key, new int[] { pos.getX(), pos.getY(), pos.getZ() });
    }

    public static @Nullable NbtCompound getCompoundOrNull(NbtCompound nbt, String key) {
        return nbt.contains(key, NbtElement.COMPOUND_TYPE) ? nbt.getCompound(key) : null;
    }
}
