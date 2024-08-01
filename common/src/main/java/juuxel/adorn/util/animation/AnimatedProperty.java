package juuxel.adorn.util.animation;

public final class AnimatedProperty<T> extends AbstractAnimatedProperty<T> {
    private T value;

    public AnimatedProperty(T initial, AnimationEngine engine, int duration, Interpolator<T> interpolator) {
        super(engine, duration, interpolator);
        value = initial;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    protected void setRawValue(T value) {
        this.value = value;
    }
}
