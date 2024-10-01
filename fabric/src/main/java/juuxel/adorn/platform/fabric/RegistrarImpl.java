package juuxel.adorn.platform.fabric;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public final class RegistrarImpl<T> implements Registrar<T> {
    private final Registry<T> registry;
    private final List<T> objects = new ArrayList<>();

    public RegistrarImpl(Registry<T> registry) {
        this.registry = registry;
    }

    @Override
    public <U extends T> Registered.WithKey<T, U> register(String id, Supplier<? extends U> provider) {
        var key = RegistryKey.of(registry.getKey(), AdornCommon.id(id));
        var registered = Registry.register(registry, key, provider.get());
        objects.add(registered);
        return new Registered.WithKey<>() {
            @Override
            public U get() {
                return registered;
            }

            @Override
            public RegistryKey<T> key() {
                return key;
            }
        };
    }

    @Override
    public Iterator<T> iterator() {
        return objects.iterator();
    }
}
