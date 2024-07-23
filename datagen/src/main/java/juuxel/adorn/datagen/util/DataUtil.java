package juuxel.adorn.datagen.util;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public final class DataUtil {
    public static <T, K> Predicate<T> distinctBy(Function<T, K> keyGetter) {
        Set<K> keys = new HashSet<>();
        return t -> keys.add(keyGetter.apply(t));
    }
}
