package juuxel.adorn.util;

import net.minecraft.nbt.NbtCompound;

public interface NbtConvertible {
    void readNbt(NbtCompound nbt);
    NbtCompound writeNbt(NbtCompound nbt);
}
