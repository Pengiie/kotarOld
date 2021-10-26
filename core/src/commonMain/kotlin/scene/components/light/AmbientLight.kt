package dev.pengie.kotaro.scene.components.light

import dev.pengie.kotaro.data.Color

/**
 * A light that illuminates the entire scene.
 *
 * @param color the color of the light.
 * @param strength the intensity of the light on the entire scene.
 */
class AmbientLight(color: Color = Color.White, val strength: Float = 0.2f) : Light(color)