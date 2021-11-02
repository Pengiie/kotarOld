package dev.pengie.kotaro.graphics

import dev.pengie.kotaro.data.ColorFormat
import dev.pengie.kotaro.data.Interpolation.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.GL_RG
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.NULL

open class OpenGLTexture : Texture {
    internal var id: Int = -1

    override fun init(data: TexData) {
        id = glGenTextures()
        bind()
        if(data.data.isEmpty())
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, data.width, data.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL)
        else {
            val buf = MemoryUtil.memAlloc(data.data.size)
            buf.put(data.data)
            buf.flip()
            val format = when(data.format) {
                ColorFormat.G -> GL_RED
                ColorFormat.GA -> GL_RG
                ColorFormat.RGB -> GL_RGB
                ColorFormat.RGBA -> GL_RGBA
            }
            glTexImage2D(GL_TEXTURE_2D, 0, format, data.width, data.height, 0, format, GL_UNSIGNED_BYTE, buf)
        }

        val interpolation = when(data.interpolation) {
            LINEAR -> GL_LINEAR
            NEAREST -> GL_NEAREST
        }
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, interpolation)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, interpolation)
        unbind()
    }

    override fun bind() {
        glBindTexture(GL_TEXTURE_2D, id)
    }

    override fun unbind() {
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    override fun dispose() {
        glDeleteTextures(id)
    }
}

actual object TextureFactory {
    actual operator fun invoke(): Texture = OpenGLTexture()
}