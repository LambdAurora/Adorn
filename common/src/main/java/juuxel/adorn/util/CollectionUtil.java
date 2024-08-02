package juuxel.adorn.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

public final class CollectionUtil {
    /**
     * Interleaves elements of all lists.
     *
     * <p>For example, if this method is passed the list {@code [[a, b, c], [A, B, C, D]]},
     * the expected output is {@code [a, A, b, B, c, C, D]}.
     */
    public static <E> List<E> interleave(List<List<E>> lists) {
        var size = sumOf(lists, List::size);
        var maxSize = maxOf(lists, List::size);
        List<E> output = new ArrayList<>(size);

        for (int i = 0; i < maxSize; i++) {
            for (var list : lists) {
                if (i < list.size()) {
                    output.add(list.get(i));
                }
            }
        }

        return output;
    }

    public static <T> int sumOf(Iterable<T> ts, ToIntFunction<? super T> getter) {
        int sum = 0;
        for (T t : ts) {
            sum += getter.applyAsInt(t);
        }
        return sum;
    }

    public static <T> int maxOf(Iterable<T> ts, ToIntFunction<? super T> getter) {
        int max = Integer.MIN_VALUE;
        for (T t : ts) {
            var current = getter.applyAsInt(t);
            if (current > max) max = current;
        }
        return max;
    }
}
