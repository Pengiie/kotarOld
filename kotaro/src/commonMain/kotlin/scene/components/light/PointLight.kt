package dev.pengie.kotaro.scene.components.light

import dev.pengie.kotaro.data.Color

class PointLight(color: Color = Color.White, val strength: Float = 1f) : Light(color)