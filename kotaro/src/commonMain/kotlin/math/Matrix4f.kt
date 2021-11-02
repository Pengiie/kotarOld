package dev.pengie.kotaro.math

import dev.pengie.kotaro.scene.components.Transform
import kotlin.math.*

class Matrix4f : Matrix4<Float>(0f, ArithmeticFloat) {
    override fun copy(): Matrix4<Float> {
        TODO("Not yet implemented")
    }

    /**
     * Returns a column-major array representation of this matrix
     */
    override fun toArray(): Array<Float> = arrayOf(
        m00, m10, m20, m30,
        m01, m11, m21, m31,
        m02, m12, m22, m32,
        m03, m13, m23, m33
    )

    override fun translate(x: Float, y: Float, z: Float) {
        m03 = x
        m13 = y
        m23 = z
    }

    override fun rotate(quaternion: Quaternion) {
        reassign((Matrix3f.rotationMatrix(quaternion).toMatrix4f() * this).toMatrix4f())
    }

    override fun rotateX(angle: Float) {
        reassign((Matrix3f.rotationMatrixX(angle).toMatrix4f() * this).toMatrix4f())
    }

    override fun rotateY(angle: Float) {
        reassign((Matrix3f.rotationMatrixY(angle).toMatrix4f() * this).toMatrix4f())
    }

    override fun rotateZ(angle: Float) {
        reassign((Matrix3f.rotationMatrixZ(angle).toMatrix4f() * this).toMatrix4f())
    }

    private fun reassign(r: Matrix4f) {
        m00 = r.m00
        m01 = r.m01
        m02 = r.m02
        m03 = r.m03

        m10 = r.m10
        m11 = r.m11
        m12 = r.m12
        m13 = r.m13

        m20 = r.m20
        m21 = r.m21
        m22 = r.m22
        m23 = r.m23

        m30 = r.m30
        m31 = r.m31
        m32 = r.m32
        m33 = r.m33
    }

    override fun scale(x: Float, y: Float, z: Float) {
        m00 = x
        m11 = y
        m22 = z
    }

    override fun times(other: Matrix4<Float>): Matrix4<Float> =
        Matrix4f().also {
            // First Row
            it.m00 = m00 * other.m00 + m01 * other.m10 + m02 * other.m20 + m03 * other.m30
            it.m01 = m00 * other.m01 + m01 * other.m11 + m02 * other.m21 + m03 * other.m31
            it.m02 = m00 * other.m02 + m01 * other.m12 + m02 * other.m22 + m03 * other.m32
            it.m03 = m00 * other.m03 + m01 * other.m13 + m02 * other.m23 + m03 * other.m33

            // Second Row
            it.m10 = m10 * other.m00 + m11 * other.m10 + m12 * other.m20 + m13 * other.m30
            it.m11 = m10 * other.m01 + m11 * other.m11 + m12 * other.m21 + m13 * other.m31
            it.m12 = m10 * other.m02 + m11 * other.m12 + m12 * other.m22 + m13 * other.m32
            it.m13 = m10 * other.m03 + m11 * other.m13 + m12 * other.m23 + m13 * other.m33

            // Third Row
            it.m20 = m20 * other.m00 + m21 * other.m10 + m22 * other.m20 + m23 * other.m30
            it.m21 = m20 * other.m01 + m21 * other.m11 + m22 * other.m21 + m23 * other.m31
            it.m22 = m20 * other.m02 + m21 * other.m12 + m22 * other.m22 + m23 * other.m32
            it.m23 = m20 * other.m03 + m21 * other.m13 + m22 * other.m23 + m23 * other.m33

            // Fourth Row
            it.m30 = m30 * other.m00 + m31 * other.m10 + m32 * other.m20 + m33 * other.m30
            it.m31 = m30 * other.m01 + m31 * other.m11 + m32 * other.m21 + m33 * other.m31
            it.m32 = m30 * other.m02 + m31 * other.m12 + m32 * other.m22 + m33 * other.m32
            it.m33 = m30 * other.m03 + m31 * other.m13 + m32 * other.m23 + m33 * other.m33
        }

    override fun toString(): String {
        return """
            [$m00, $m01, $m02, $m03]
            [$m10, $m11, $m12, $m13]
            [$m20, $m21, $m22, $m23]
            [$m30, $m31, $m32, $m33]
        """.trimIndent()
    }

    companion object {
        fun identity(): Matrix4f =
            Matrix4f().apply {
                m00 = 1f
                m11 = 1f
                m22 = 1f
                m33 = 1f
            }
        fun perspective(fov: Float, aspect: Float, near: Float, far: Float): Matrix4f =
            Matrix4f().apply {
                m00 = 1f / (aspect * tan(fov / 2f))
                m11 = 1f / tan(fov / 2f)
                m22 = -(far + near) / (far - near)
                m23 = -(2 * far * near) / (far - near)
                m32 = -1f
            }
    }
}

fun Matrix4<Float>.toMatrix4f(): Matrix4f = this as Matrix4f
