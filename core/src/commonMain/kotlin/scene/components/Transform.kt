package dev.pengie.kotaro.scene.components

import dev.pengie.kotaro.math.*
import kotlin.math.asin
import kotlin.math.atan2

class Transform(var position: Vector3f = Vector3f(), var rotation: Quaternion = Quaternion(), var scale: Vector3f = Vector3f(1f)) {
    val forward: Vector3f
        get() = calculateForward()
    val right: Vector3f
        get() = calculateRight()
    val up: Vector3f
        get() = calculateUp()

    fun lookAt(position: Vector3f) {
        rotation = Quaternion.angle((position - this.position).toVector3f().normalize(), Vector3f.forward)
    }

    private fun calculateRotationMatrix(): Matrix3f =
        Matrix3f.rotationMatrix(rotation)

    private fun calculateForward(): Vector3f = rotation.rotate(Vector3f.forward)
    private fun calculateRight(): Vector3f = rotation.rotate(Vector3f.right)
    private fun calculateUp(): Vector3f = rotation.rotate(Vector3f.up)
}

fun Transform.toModelMatrix(): Matrix4f =
    Matrix4f.identity().also {
        it.scale(this.scale.x, this.scale.y, this.scale.z)
        it.rotate(rotation)
        it.translate(this.position.x, this.position.y, this.position.z)
    }

fun Transform.toViewMatrix(): Matrix4f =
    Matrix4f.identity().also {
        it.translate(-this.position.x, -this.position.y, -this.position.z)
        it.rotate(rotation.inverse())
    }