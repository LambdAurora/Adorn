package juuxel.adorn.platform.fabric;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import net.minecraft.registry.Registry;

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
    public <U extends T> Registered<U> register(String id, Supplier<? extends U> provider) {
        var registered = Registry.register(registry, AdornCommon.id(id), provider.get());
        objects.add(registered);
        return () -> registered;
    }

    @Override
    public Iterator<T> iterator() {
        return objects.iterator();
    }
}
