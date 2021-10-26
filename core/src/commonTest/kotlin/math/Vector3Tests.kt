package math

import dev.pengie.kotaro.math.Vector3f
import kotlin.test.Test
import kotlin.test.assertEquals

class Vector3Tests {

    @Test
    fun testCrossProduct() {
        val cross = Vector3f.cross(Vector3f(2f, 3f, 4f), Vector3f(5f, 6f, 7f))
        assertEquals(-3f, cross.x, 0.0001f)
        assertEquals(6f, cross.y, 0.0001f)
        assertEquals(-3f, cross.z, 0.0001f)
    }
}