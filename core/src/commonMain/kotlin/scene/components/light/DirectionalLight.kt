package dev.pengie.kotaro.scene.components.light

import dev.pengie.kotaro.data.Color

class DirectionalLight(color: Color = Color.White, val strength: Float = 0.5f) : Light(color)