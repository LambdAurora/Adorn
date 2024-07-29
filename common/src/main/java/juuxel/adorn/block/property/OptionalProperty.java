package juuxel.adorn.block.property;

import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class OptionalProperty<T extends Enum<T> & StringIdentifiable> extends Property<OptionalProperty.Value<T>> {
    private static final String NONE_NAME = "none";

    private final EnumProperty<T> delegate;
    private final Value.None<T> none = new Value.None<>();
    private final Map<@Nullable T, Value<T>> values;

    @SuppressWarnings("unchecked")
    public OptionalProperty(EnumProperty<T> delegate) {
        super(delegate.getName(), (Class<Value<T>>) (Class<?>) Value.class);
        this.delegate = delegate;

        values = new HashMap<>();
        values.put(null, none);
        for (T value : delegate.getValues()) {
            if (NONE_NAME.equals(value.asString())) {
                throw new IllegalArgumentException("Delegate has a 'none' value");
            }

            values.put(value, new Value.Some<>(value));
        }
    }

    @Override
    public Optional<Value<T>> parse(String name) {
        return NONE_NAME.equals(name) ? Optional.of(none) : delegate.parse(name).map(values::get);
    }

    @Override
    public Collection<Value<T>> getValues() {
        return values.values();
    }

    @Override
    public String name(Value<T> value) {
        // TODO: Use pattern matching
        return value instanceof Value.Some<T> some ? some.value.asString() : NONE_NAME;
    }

    public EnumProperty<T> getDelegate() {
        return delegate;
    }

    public Value.None<T> getNone() {
        return none;
    }

    public @Nullable Value<T> wrap(@Nullable T value) {
        return values.get(value);
    }

    public Value<T> wrapOrNone(@Nullable T value) {
        return values.getOrDefault(value, none);
    }

    public sealed interface Value<T> extends Comparable<Value<T>> {
        boolean isPresent();
        @Nullable T value();

        record Some<T extends Enum<T> & StringIdentifiable>(T value) implements Value<T> {
            @Override
            public boolean isPresent() {
                return true;
            }

            @Override
            public int compareTo(OptionalProperty.Value<T> o) {
                // TODO: Use pattern matching
                if (o instanceof Some<T> other) {
                    return value.compareTo(other.value);
                }

                return 1;
            }
        }

        final class None<T extends Enum<T> & StringIdentifiable> implements Value<T> {
            @Override
            public @Nullable T value() {
                return null;
            }

            @Override
            public boolean isPresent() {
                return false;
            }

            @Override
            public int compareTo(OptionalProperty.Value<T> o) {
                return o instanceof None<T> ? 0 : -1;
            }
        }
    }
}
