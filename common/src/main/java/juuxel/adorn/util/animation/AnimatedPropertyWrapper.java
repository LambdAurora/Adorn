package juuxel.adorn.util.animation;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class AnimatedPropertyWrapper<T> extends AbstractAnimatedProperty<T> {
    private final Supplier<T> getter;
    private final Consumer<T> setter;

    public AnimatedPropertyWrapper(AnimationEngine engine, int duration, Interpolator<T> interpolator, Supplier<T> getter, Consumer<T> setter) {
        super(engine, duration, interpolator);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public T get() {
        return getter.get();
    }

    @Override
    protected void setRawValue(T value) {
        setter.accept(value);
    }
}
