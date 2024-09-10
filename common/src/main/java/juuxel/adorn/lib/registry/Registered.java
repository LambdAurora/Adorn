package juuxel.adorn.lib.registry;

import net.minecraft.registry.RegistryKey;

import java.util.function.Supplier;

public interface Registered<T> extends Supplier<T> {
    RegistryKey<? super T> key();
}
