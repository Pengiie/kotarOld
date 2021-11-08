package dev.pengie.kotaro.graphics

import dev.pengie.kotaro.data.Color
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil.NULL

private var cullFacesMode: Boolean? = null
private var wireframeMode: Boolean? = null
private var depthTestMode: Boolean? = null

class OpenGLBatchRenderer : BatchRenderer<OpenGLModel>() {

    override fun bindWindowLayer() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    override fun viewport(width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    override fun clearScreen() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    override fun clearColor(color: Color) {
        glClearColor(color.r / 255f, color.g / 255f, color.b / 255f, color.a / 255f)
    }

    override fun renderBatch() {
        for((model, meshRenderer) in batch) {
            if(wireframeMode == null || meshRenderer.wireframe != wireframeMode) {
                wireframeMode = meshRenderer.wireframe
                glPolygonMode(GL_FRONT_AND_BACK, if(wireframeMode!!) GL_LINE else GL_FILL)
            }
            if(cullFacesMode == null || meshRenderer.cullFaces != cullFacesMode) {
                cullFacesMode = meshRenderer.cullFaces
                if(cullFacesMode!!) {
                    glEnable(GL_CULL_FACE)
                    glCullFace(GL_BACK)
                } else
                    glDisable(GL_CULL_FACE)
            }
            if(depthTestMode == null) {
                depthTestMode = true
                glEnable(GL_DEPTH_TEST)
            }
            glEnable(GL_BLEND)
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
            model.bind()
            glEnableVertexAttribArray(0)
            glEnableVertexAttribArray(1)
            glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, NULL)

        }
    }


}

actual object RendererFactory {
    @Suppress("UNCHECKED_CAST")
    actual operator fun invoke(): BatchRenderer<Model> = OpenGLBatchRenderer() as BatchRenderer<Model>
}