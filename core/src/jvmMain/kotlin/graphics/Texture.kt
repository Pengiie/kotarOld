package dev.pengie.kotaro.graphics

import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL

class OpenGLTexture : Texture {
    internal var id: Int = -1

    override fun init(data: TexData) {
        id = glGenTextures()
        bind()
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, data.width, data.height, 0, GL_RGB, GL_UNSIGNED_BYTE, NULL)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
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