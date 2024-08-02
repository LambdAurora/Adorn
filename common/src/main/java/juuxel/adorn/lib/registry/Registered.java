package juuxel.adorn.lib.registry;

import java.util.function.Supplier;

@FunctionalInterface
public interface Registered<T> extends Supplier<T> {
}
