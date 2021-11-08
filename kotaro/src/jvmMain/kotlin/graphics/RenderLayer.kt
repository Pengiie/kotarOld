package dev.pengie.kotaro.graphics

import org.lwjgl.opengl.GL30.*

class OpenGLRenderLayer(width: Int, height: Int, type: RenderLayerType) : RenderLayer(width, height, type) {

    private var id: Int = -1
    private var texture: OpenGLTexture = OpenGLTexture()
    private var depthTexture: OpenGLDepthTexture = OpenGLDepthTexture()

    override fun init() {
        id = glGenFramebuffers()
        bind()
        depthTexture.init(TexData(width = width, height = height))
        if(type == RenderLayerType.DEFAULT) {
            texture.init(TexData(width = width, height = height))
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture.id, 0)
        }
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthTexture.id, 0)
        unbind()
    }

    override fun bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, id)
    }

    override fun unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    override fun getTexture(): Texture = when(type) {
        RenderLayerType.DEFAULT -> texture
        RenderLayerType.DEPTH -> depthTexture
    }

    override fun dispose() {
        glDeleteFramebuffers(id)
        if(type == RenderLayerType.DEFAULT)
            texture.dispose()
        depthTexture.dispose()
    }
}

actual object RenderLayerFactory {
    actual operator fun invoke(width: Int, height: Int, type: RenderLayerType): RenderLayer = OpenGLRenderLayer(width, height, type)
}