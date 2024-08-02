package juuxel.adorn.util

import java.util.Optional
import java.util.ServiceLoader

inline fun <reified T> loadService(): T =
    ServiceLoader.load(T::class.java).findFirst().unwrapService(T::class.java)

fun <T> Optional<T>.unwrapService(type: Class<T>): T =
    orElseThrow { RuntimeException("Could not find Adorn platform service ${type.simpleName}") }
