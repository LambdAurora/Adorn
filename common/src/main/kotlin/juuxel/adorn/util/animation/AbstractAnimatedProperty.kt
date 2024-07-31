package juuxel.adorn.util.animation

import kotlin.math.pow

abstract class AbstractAnimatedProperty<T>(
    private val engine: AnimationEngine,
    private val duration: Int,
    private val interpolator: Interpolator<T>
) {
    protected abstract fun setRawValue(value: T)

    @Volatile
    private var currentTask: Task? = null

    abstract fun get(): T

    fun set(value: T) {
        synchronized(this) {
            val oldValue = get()
            currentTask?.let(engine::remove)

            if (oldValue != value) {
                val task = Task(oldValue, value)
                currentTask = task
                engine.add(task)
            }
        }
    }

    companion object {
        // https://easings.net/#easeOutQuint
        private fun ease(delta: Float): Float =
            1 - (1 - delta).pow(5)
    }

    private inner class Task(private val from: T, private val to: T) : AnimationTask {
        private var age = 0

        override fun isAlive(): Boolean = age < duration

        override fun tick() {
            age++
            val delta = ease(age.toFloat() / duration.toFloat())
            val newValue = interpolator.interpolate(delta, from, to)
            setRawValue(newValue)
        }

        override fun removed() {
            synchronized(this@AbstractAnimatedProperty) {
                if (currentTask == this) {
                    currentTask = null
                }
            }
        }
    }
}
