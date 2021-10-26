package dev.pengie.kotaro.math

import kotlin.math.*

class Quaternion(var w: Float = 1f, var x: Float = 0f, var y: Float = 0f, var z: Float = 0f) {
    val eulerAngles: Vector3f
        get() {
            val s = 2*(w*y-z*x)
            val sign = if(s > 0) 1f else -1f
            return Vector3f(
                atan2(2*(w*x+y*z), 1-2*(x*x+y*y)).toDegrees(),
                asin(abs(s)).toDegrees() * sign,
                atan2(2*(w*z+x*y), 1-2*(y*y+z*z)).toDegrees()
            )
        }

    operator fun times(q: Quaternion): Quaternion =
        Quaternion(
            w * q.w - x * q.x - y * q.y - z * q.z,
            w * q.x + x * q.w + y * q.z - z * q.y,
            w * q.y + y * q.w - x * q.z + z * q.x,
            w * q.z + z * q.w + x * q.y - y * q.x,
        )

    fun length(): Float = sqrt(w*w + x*x + y*y + z*z)

    fun normalize(): Quaternion {
        val length = length()
        return Quaternion(
            w / length,
            x / length,
            y / length,
            z / length
        )
    }

    fun inverse(): Quaternion = Quaternion(w, -x, -y, -z)

    fun rotate(v: Vector3f): Vector3f {
        val q = this * Quaternion(0f, v.x, v.y, v.z) * inverse()
        return Vector3f(q.x, q.y, q.z)
    }

    override fun toString(): String = "Quaternion($w + ${x}i + ${y}j + ${z}k)"

    companion object {
        /**
         * Returns a quaternion that is equal to the given euler angles in degrees.
         * @param x the x rotation in degrees.
         * @param y the y rotation in degrees.
         * @param z the z rotation in degrees.
         * @return the quaternion that is analogous to the given euler angles.
         */
        fun euler(x: Float, y: Float, z: Float): Quaternion {
            val xr = x.toRadians()
            val yr = y.toRadians()
            val zr = z.toRadians()
            val cz = cos(zr * 0.5f)
            val sz = sin(zr * 0.5f)
            val cp = cos(yr * 0.5f)
            val sp = sin(yr * 0.5f)
            val cx = cos(xr * 0.5f)
            val sx = sin(xr * 0.5f)

            return Quaternion(
                cx * cp * cz + sx * sp * sz,
                sx * cp * cz - cx * sp * sz,
                cx * sp * cz + sx * cp * sz,
                cx * cp * sz + sx * sp * cz
            )
        }

        /**
         * Returns a quaternion that represents a rotation around the provided axis.
         * @param angle the angle in degrees.
         * @param axis the normalized vector that is rotated around.
         * @return a quaternion which represents a rotation around an axis.
         */
        fun axisAngle(angle: Float, axis: Vector3f): Quaternion {
            val rad = angle.toRadians()
            return Quaternion(
                cos(rad / 2f),
                axis.x * sin(rad / 2f),
                axis.y * sin(rad / 2f),
                axis.z * sin(rad / 2f)
            ).normalize()
        }

        /**
         * Returns a quaternion that represents the rotation between two normalized vectors.
         * @param left the normalized left side vector.
         * @param right the normalized right side vector.
         * @return a quaternion which represents the rotation between both vectors.
         */
        fun angle(left: Vector3f, right: Vector3f = Vector3f.up): Quaternion {
            val angle = Vector3f.dot(left, right)
            val rotAxis = Vector3f.cross(right, left)
            return Quaternion(
                angle + 1f,
                rotAxis.x,
                rotAxis.y,
                rotAxis.z
            ).normalize()
        }
    }
}