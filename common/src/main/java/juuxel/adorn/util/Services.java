package juuxel.adorn.util;

import java.util.ServiceLoader;

public final class Services {
    public static <T> T load(Class<T> c) {
        return ServiceLoader.load(c)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Could not find Adorn platform service " + c.getSimpleName()));
    }
}
