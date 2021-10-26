package dev.pengie.kotaro.graphics.shader

import dev.pengie.kotaro.data.Color
import dev.pengie.kotaro.graphics.Texture
import dev.pengie.kotaro.graphics.shader.builder.ShaderBuilder
import dev.pengie.kotaro.math.Matrix4f
import dev.pengie.kotaro.math.Vector3f

interface Shader {
    fun init()
    fun start()
    fun stop()

    fun uniformBool(location: String, value: Boolean)
    fun uniformFloat(location: String, value: Float)
    fun uniformVector3f(location: String, vector: Vector3f)
    //fun uniformVector4f(location: String, vector: Vector4f)
    fun uniformMatrix4f(location: String, matrix: Matrix4f)

    fun uniformColor(location: String, color: Color)
    fun uniformTexture(location: String, texture: Texture)
}

expect object ShaderFactory {
    fun createShader(builder: ShaderBuilder): Shader
}