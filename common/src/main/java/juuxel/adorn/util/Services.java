package juuxel.adorn.util;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.ServiceLoader;

public final class Services {
    private static final Map<Class<?>, Object> CACHE = new IdentityHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T load(Class<T> c) {
        return (T) CACHE.computeIfAbsent(c, Services::loadService);
    }

    private static <T> T loadService(Class<T> c) {
        return ServiceLoader.load(c)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Could not find Adorn platform service " + c.getSimpleName()));
    }
}
