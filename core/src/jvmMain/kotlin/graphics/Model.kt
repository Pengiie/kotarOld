package dev.pengie.kotaro.graphics

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.ShortBuffer

class OpenGLModel : Model() {
    private var vao = 0;
    private val vbos: MutableList<Int> = mutableListOf()

    private var vertexCount = 0

    override fun create(mesh: Mesh) {
        vao = glGenVertexArrays()
        vertexCount = mesh.indices.size
        bind()

        createEbo(mesh.indices)
        createAttrib(0, 3, mesh.vertices)
        createAttrib(1, 2, mesh.textureCoords)
    }

    fun bind() { glBindVertexArray(vao) }

    private fun createAttrib(index: Int, size: Int, data: List<Float>) {
        val buffer = glGenBuffers().apply { vbos.add(this) }
        glBindBuffer(GL_ARRAY_BUFFER, buffer)
        val buf: FloatBuffer = MemoryUtil.memAllocFloat(data.size)
        buf.put(data.toFloatArray())
        buf.flip()
        GL15.glBufferData(GL_ARRAY_BUFFER, buf, GL_STATIC_DRAW)
        GL20.glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0)
        glEnableVertexAttribArray(index)
    }

    private fun createEbo(data: List<Int>) {
        val buffer = glGenBuffers().apply { vbos.add(this) }
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffer)
        val buf: IntBuffer = MemoryUtil.memAllocInt(data.size)
        buf.put(data.toIntArray())
        buf.flip()
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buf, GL_STATIC_DRAW)
    }

    override fun dispose() {
        glDeleteVertexArrays(vao)
        glDeleteBuffers(vbos.toIntArray())
    }

    fun getVertexCount(): Int = vertexCount
}

actual object ModelFactory {
    actual fun createModel(): Model = OpenGLModel()
}