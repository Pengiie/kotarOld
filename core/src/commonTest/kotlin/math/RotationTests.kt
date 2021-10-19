package math

import dev.pengie.kotaro.math.Matrix3f
import dev.pengie.kotaro.math.Vector2f
import dev.pengie.kotaro.math.Vector3f
import dev.pengie.kotaro.math.toRadians
import kotlin.test.Test
import kotlin.test.assertEquals

class RotationTests {

    @Test
    fun testYRotation() {
        val forward = Vector3f.forward
        val rotated180 = Matrix3f.rotationMatrixY(180f.toRadians()) * forward
        assertEquals(Vector3f(0f, 0f, -1f), rotated180)
    }
}