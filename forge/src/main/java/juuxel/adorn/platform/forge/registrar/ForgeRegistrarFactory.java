package juuxel.adorn.platform.forge.registrar;

import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public final class ForgeRegistrarFactory implements RegistrarFactory {
    @Override
    public <T> Registrar<T> create(RegistryKey<Registry<T>> registryKey) {
        return new DeferredRegistrar<>(registryKey);
    }
}
