package juuxel.adorn.util.animation

class AnimatedProperty<T>(
    initial: T,
    engine: AnimationEngine,
    duration: Int,
    interpolator: Interpolator<T>
) : AbstractAnimatedProperty<T>(engine, duration, interpolator) {
    private var value: T = initial

    override fun get(): T = value
    override fun setRawValue(value: T) {
        this.value = value
    }
}
