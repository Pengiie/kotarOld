package dev.pengie.kotaro.graphics.shader

import dev.pengie.kotaro.math.Matrix4f
import org.lwjgl.opengl.GL20.*

const val MAIN_VERTEX = """
    #version 400 core
    layout(location = 0) in vec3 position;
    uniform mat4 ${Uniforms.PROJ_VIEW_MATRIX};
    uniform mat4 ${Uniforms.MODEL_MATRIX};
    void main() {
      gl_Position = ${Uniforms.PROJ_VIEW_MATRIX} * ${Uniforms.MODEL_MATRIX} * vec4(position, 1.0);
    }
    """

const val MAIN_FRAGMENT = """
    #version 400 core
    out vec4 outColor;
    void main() {
      outColor = vec4(1.0, 1.0, 1.0, 1.0);
    }
    """

const val SCREEN_VERTEX = """
    #version 400 core
    layout(location = 0) in vec3 position;
    layout(location = 1) in vec2 textureCoord;
    out vec2 passTextureCoord;
    void main() {
      gl_Position = vec4(position, 1.0);
      passTextureCoord = textureCoord;
    }
    """

const val SCREEN_FRAGMENT = """
    #version 400 core
    in vec2 passTextureCoord;
    out vec4 outColor;
    uniform sampler2D screen;
    void main() {
      outColor = texture(screen, passTextureCoord);
    }
    """

class OpenGLShader(private val vertexSource: String, private val fragmentSource: String) : Shader {
    private var program: Int = 0;

    override fun init() {
        program = glCreateProgram()
        val vertex = compileShader(vertexSource, GL_VERTEX_SHADER)
        val fragment = compileShader(fragmentSource, GL_FRAGMENT_SHADER)

        glAttachShader(program, vertex)
        glAttachShader(program, fragment)
        glLinkProgram(program)
        glDetachShader(program, vertex)
        glDetachShader(program, fragment)
        glDeleteShader(vertex)
        glDeleteShader(fragment)
    }

    override fun start() {
        glUseProgram(program)
    }

    override fun stop() {
        glUseProgram(0)
    }

    private fun getUniformLocation(name: String): Int = glGetUniformLocation(program, name)

    override fun uniformMatrix4f(location: String, matrix: Matrix4f) {
        glUniformMatrix4fv(getUniformLocation(location), false, matrix.toArray().toFloatArray())
    }

    private fun compileShader(source: String, type: Int): Int {
        val id = glCreateShader(type)
        glShaderSource(id, source)
        glCompileShader(id)
        if(glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE)
            throw RuntimeException(glGetShaderInfoLog(id))

        return id
    }
}

actual object ShaderFactory {
    actual fun createMainShader(): Shader = OpenGLShader(MAIN_VERTEX, MAIN_FRAGMENT)
    actual fun createScreenShader(): Shader = OpenGLShader(SCREEN_VERTEX, SCREEN_FRAGMENT)
}