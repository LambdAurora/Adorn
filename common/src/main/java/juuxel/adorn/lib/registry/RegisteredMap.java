package juuxel.adorn.lib.registry;

import com.google.common.base.Suppliers;

import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class RegisteredMap<K, V> implements Registered<Map<K, V>> {
    private final Map<K, Registered<? extends V>> map;
    private final Supplier<Map<K, V>> builtMap;

    private RegisteredMap(MapFactory<K> mapFactory, Map<K, Registered<? extends V>> map) {
        this.map = map;
        this.builtMap = Suppliers.memoize(() -> {
            Map<K, V> result = mapFactory.create();
            this.map.forEach((key, value) -> result.put(key, value.get()));
            return Collections.unmodifiableMap(result);
        });
    }

    public Registered<? extends V> get(K key) {
        return map.get(key);
    }

    public V getEager(K key) {
        return get(key).get();
    }

    @Override
    public Map<K, V> get() {
        return builtMap.get();
    }

    public Stream<V> values() {
        return map.values().stream().map(Registered::get);
    }

    public void forEach(BiConsumer<? super K, ? super V> action) {
        map.forEach((key, value) -> action.accept(key, value.get()));
    }

    public static <K, V> Builder<K, V> builder(MapFactory<K> mapFactory) {
        return new Builder<>(mapFactory, mapFactory.create());
    }

    public static final class Builder<K, V> {
        private final MapFactory<K> mapFactory;
        private final Map<K, Registered<? extends V>> backing;

        private Builder(MapFactory<K> mapFactory, Map<K, Registered<? extends V>> backing) {
            this.mapFactory = mapFactory;
            this.backing = backing;
        }

        public Builder<K, V> put(K key, Registered<? extends V> value) {
            backing.put(key, value);
            return this;
        }

        public RegisteredMap<K, V> build() {
            return new RegisteredMap<>(mapFactory, backing);
        }
    }

    @FunctionalInterface
    public interface MapFactory<K> {
        <V> Map<K, V> create();

        static <K extends Enum<K>> MapFactory<K> enumKeys(Class<K> keyClass) {
            return new MapFactory<>() {
                @Override
                public <V> Map<K, V> create() {
                    return new EnumMap<>(keyClass);
                }
            };
        }

        static <K> MapFactory<K> linkedHashKeys() {
            return LinkedHashMap::new;
        }
    }
}
