package juuxel.adorn.lib.registry

import java.util.function.Supplier
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun interface Registered<out T> : Supplier<@UnsafeVariance T>, ReadOnlyProperty<Any?, T> {
    override fun get(): T

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = get()
}
