package juuxel.adorn.util

@JvmOverloads
fun color(rgb: Int, alpha: Int = 0xFF) =
    alpha shl 24 or rgb

fun color(rgb: Int, alpha: Float) = color(rgb, (alpha * 255f).toInt())

fun color(red: Float, green: Float, blue: Float, alpha: Float = 1f): Int =
    color((red * 255f).toInt(), (green * 255f).toInt(), (blue * 255f).toInt(), (alpha * 255f).toInt())

fun color(red: Int, green: Int, blue: Int, alpha: Int = 0xFF): Int =
    ((alpha and 0xFF) shl 24) or ((red and 0xFF) shl 16) or ((green and 0xFF) shl 8) or (blue and 0xFF)

object Colors {
    @JvmField
    val BLACK = color(0x000000)

    @JvmField
    val WHITE = color(0xFFFFFF)

    @JvmField
    val SCREEN_TEXT = color(0x404040)

    @JvmField
    val TRANSPARENT = color(0x000000, alpha = 0)

    @JvmStatic
    fun redOf(argb: Int): Float = ((argb shr 16) and 0xFF) / 255f

    @JvmStatic
    fun greenOf(argb: Int): Float = ((argb shr 8) and 0xFF) / 255f

    @JvmStatic
    fun blueOf(argb: Int): Float = (argb and 0xFF) / 255f

    @JvmStatic
    fun alphaOf(argb: Int): Float = ((argb shr 24) and 0xFF) / 255f
}
