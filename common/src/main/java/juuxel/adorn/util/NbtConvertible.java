package juuxel.adorn.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public interface NbtConvertible {
    void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries);
    NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries);
}
