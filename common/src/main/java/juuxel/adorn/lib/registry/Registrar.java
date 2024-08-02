package juuxel.adorn.lib.registry;

import java.util.function.Supplier;

public interface Registrar<T> extends Iterable<T> {
    /**
     * Registers an object with the id. The object is created using the provider.
     */
    <U extends T> Registered<U> register(String id, Supplier<? extends U> provider);
}
