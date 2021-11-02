package dev.pengie.kotaro.data

/**
 * A color representing rgba.
 * @param r red component.
 * @param g green component.
 * @param b blue component.
 * @param a alpha component.
 */
class Color(val r: Int, val g: Int, val b: Int, val a: Int) {
    /**
     * Hex representation of a color.
     * @param hex the hex code of the color including alpha.
     */
    constructor(hex: Long) : this(
        (hex.toInt() shr 24) and 0xFF,
        (hex.toInt() shr 16) and 0xFF,
        (hex.toInt() shr 8) and 0xFF,
        (hex.toInt()) and 0xFF
    )

    fun copy(): Color = Color(r, g, b, a)

    override fun toString(): String {
        return "[$r, $g, $b, $a]"
    }

    companion object {
        val White
            get() = Color(0xFFFFFFFF)
        val Black
            get() = Color(0x000000FF)
    }
}

enum class ColorFormat(val components: Int) {
    G(1),
    GA(2),
    RGB(3),
    RGBA(4);


}