package dev.pengie.kotaro.graphics.shader

import dev.pengie.kotaro.math.Matrix4f

interface Shader {
    fun init()
    fun start()
    fun stop()

    fun uniformMatrix4f(location: String, matrix: Matrix4f)
}

expect object ShaderFactory {
    fun createMainShader(): Shader
    fun createScreenShader(): Shader
}