package dev.pengie.kotaro.scene.components

import dev.pengie.kotaro.math.*

class Transform(var position: Vector3f = Vector3f(), var rotation: Vector3f = Vector3f(), var scale: Vector3f = Vector3f(1f)) {
    val forward: Vector3f
        get() = calculateForward()
    val right: Vector3f
        get() = calculateRight()
    val up: Vector3f
        get() = calculateUp()

    private fun calculateRotationMatrix(): Matrix3f =
        Matrix3f.rotationMatrix(rotation.x.toRadians(), rotation.y.toRadians(), rotation.z.toRadians())

    private fun calculateForward(): Vector3f = (calculateRotationMatrix() * Vector3f.forward).toVector3f()
    private fun calculateRight(): Vector3f = (calculateRotationMatrix() * Vector3f.right).toVector3f()
    private fun calculateUp(): Vector3f = (calculateRotationMatrix() * Vector3f.up).toVector3f()
}

fun Transform.toModelMatrix(): Matrix4f =
    Matrix4f.identity().also {
        it.scale(this.scale.x, this.scale.y, this.scale.z)
        it.rotate(rotation.x.toRadians(), rotation.y.toRadians(), rotation.z.toRadians())
        it.translate(this.position.x, this.position.y, this.position.z)
    }

fun Transform.toViewMatrix(): Matrix4f =
    Matrix4f.identity().also {

        it.translate(-this.position.x, -this.position.y, -this.position.z)
        it.rotate(-rotation.x.toRadians(), -rotation.y.toRadians(), -rotation.z.toRadians())
    }