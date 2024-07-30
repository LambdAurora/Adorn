package juuxel.adorn.platform.forge.registrar;

import com.google.common.collect.Iterators;
import juuxel.adorn.AdornCommon;
import juuxel.adorn.lib.registry.Registered;
import kotlin.jvm.functions.Function0;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class DeferredRegistrar<T> implements ForgeRegistrar<T> {
    private final DeferredRegister<T> register;
    private final List<DeferredHolder<T, ? extends T>> objects = new ArrayList<>();

    public DeferredRegistrar(RegistryKey<? extends Registry<T>> registry) {
        register = DeferredRegister.create(registry, AdornCommon.NAMESPACE);
    }

    @Override
    public void hook(IEventBus modBus) {
        register.register(modBus);
    }

    @Override
    public <U extends T> Registered<U> register(String id, Function0<? extends U> provider) {
        var registryObject = register.register(id, provider::invoke);
        objects.add(registryObject);
        return registryObject::get;
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.transform(objects.iterator(), DeferredHolder::get);
    }
}
