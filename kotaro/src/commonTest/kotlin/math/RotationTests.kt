package math

import dev.pengie.kotaro.math.*
import kotlin.test.Test
import kotlin.test.assertEquals

class RotationTests {

    @Test
    fun testMultiplication() {
        val result = Quaternion() * Quaternion.euler(0f, 90f, 0f)
        assertEquals(0.707f, result.w, 0.0002f)
        assertEquals(0.707f, result.y, 0.0002f)
    }

    @Test
    fun testYRotation() {
        val quaternion = Quaternion.euler(0f, 45f, 0f)
        val vector = Vector3f.forward
        val rotatedVector = quaternion.rotate(vector)
        assertEquals(-0.707f, rotatedVector.x, 0.0002f)
        assertEquals(-0.707f, rotatedVector.z, 0.0002f)
    }
}