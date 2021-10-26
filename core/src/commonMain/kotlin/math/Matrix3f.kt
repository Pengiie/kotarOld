package dev.pengie.kotaro.math

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class Matrix3f : Matrix3<Float>(0f, ArithmeticFloat) {
    override fun copy(): Matrix3<Float> {
        TODO("Not yet implemented")
    }

    /**
     * Returns a column-major array representation of this matrix
     */
    override fun toArray(): Array<Float> = arrayOf(
        m00, m10, m20,
        m01, m11, m21,
        m02, m12, m22,
    )

    override fun times(other: Matrix3<Float>): Matrix3<Float> =
        Matrix3f().also {
            // First Row
            it.m00 = m00 * other.m00 + m01 * other.m10 + m02 * other.m20
            it.m01 = m00 * other.m01 + m01 * other.m11 + m02 * other.m21
            it.m02 = m00 * other.m02 + m01 * other.m12 + m02 * other.m22

            // Second Row
            it.m10 = m10 * other.m00 + m11 * other.m10 + m12 * other.m20
            it.m11 = m10 * other.m01 + m11 * other.m11 + m12 * other.m21
            it.m12 = m10 * other.m02 + m11 * other.m12 + m12 * other.m22

            // Third Row
            it.m20 = m20 * other.m00 + m21 * other.m10 + m22 * other.m20
            it.m21 = m20 * other.m01 + m21 * other.m11 + m22 * other.m21
            it.m22 = m20 * other.m02 + m21 * other.m12 + m22 * other.m22
        }

    override fun times(other: Vector3<Float>): Vector3<Float> =
        Vector3f().also {
            it.x = m00 * other.x + m01 * other.y + m02 * other.z
            it.y = m10 * other.x + m11 * other.y + m12 * other.z
            it.z = m20 * other.x + m21 * other.y + m22 * other.z
        }

    fun toMatrix4f(): Matrix4f =
        Matrix4f.identity().also {
            it.m00 = m00
            it.m01 = m01
            it.m02 = m02

            it.m10 = m10
            it.m11 = m11
            it.m12 = m12

            it.m20 = m20
            it.m21 = m21
            it.m22 = m22
        }

    override fun toString(): String {
        return """
            [$m00, $m01, $m02]
            [$m10, $m11, $m12]
            [$m20, $m21, $m22]
        """.trimIndent()
    }

    companion object {
        fun identity(): Matrix3f =
            Matrix3f().apply {
                m00 = 1f
                m11 = 1f
                m22 = 1f
            }
        fun rotationMatrix(q: Quaternion): Matrix3f =
            identity().apply {
                m00 = 2 * (q.w*q.w + q.x*q.x) - 1f
                m01 = 2 * (q.x * q.y - q.w * q.z)
                m02 = 2 * (q.x * q.z + q.w * q.y)

                m10 = 2 * (q.x * q.y + q.w * q.z)
                m11 = 2 * (q.w * q.w + q.y * q.y) - 1f
                m12 = 2 * (q.y * q.z - q.w * q.x)

                m20 = 2 * (q.x * q.z - q.w * q.y)
                m21 = 2 * (q.y * q.z + q.w * q.x)
                m22 = 2 * (q.w * q.w + q.z * q.z) - 1f
            }
        fun rotationMatrix(x: Float, y: Float, z: Float): Matrix3f =
            (rotationMatrixZ(x) * rotationMatrixY(y) * rotationMatrixX(z)).toMatrix3f()
        fun rotationMatrixX(angle: Float): Matrix3f =
            identity().apply {
                val sine = sin(angle)
                val cosine = cos(angle)
                this.m11 = cosine
                this.m12 = -sine
                this.m21 = sine
                this.m22 = cosine
            }

        fun rotationMatrixY(angle: Float): Matrix3f =
            identity().apply {
                val sine = sin(angle)
                val cosine = cos(angle)
                this.m00 = cosine
                this.m02 = sine
                this.m20 = -sine
                this.m22 = cosine
            }

        fun rotationMatrixZ(angle: Float): Matrix3f =
            identity().apply {
                val sine = sin(angle)
                val cosine = cos(angle)
                this.m00 = cosine
                this.m01 = -sine
                this.m10 = sine
                this.m11 = cosine
            }
    }
}

fun Matrix3<Float>.toMatrix3f(): Matrix3f = this as Matrix3f