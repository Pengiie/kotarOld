package dev.pengie.kotaro.scene.components

import dev.pengie.kotaro.math.*
import dev.pengie.kotaro.scene.Entity
import dev.pengie.kotaro.scene.Scene
import dev.pengie.kotaro.scene.components.hierarchy.Parent

class Transform(val scene: Scene, val entity: Entity, var position: Vector3f = Vector3f(), var rotation: Quaternion = Quaternion(), var scale: Vector3f = Vector3f(1f)) {
    val forward: Vector3f
        get() = calculateForward()
    val right: Vector3f
        get() = calculateRight()
    val up: Vector3f
        get() = calculateUp()

    val globalPosition: Vector3f
        get() {
            if(scene.hasComponent<Parent>(entity)) {
                val p = scene.getComponent<Parent>(entity)!!.parent
                if(scene.hasComponent<Transform>(p))
                    return (scene.getComponent<Transform>(p)!!.position + position).toVector3f()
            }
            return position
        }
    val globalRotation: Quaternion
        get() {
            if(scene.hasComponent<Parent>(entity)) {
                val p = scene.getComponent<Parent>(entity)!!.parent
                if(scene.hasComponent<Transform>(p))
                    return scene.getComponent<Transform>(p)!!.rotation * rotation
            }
            return rotation
        }

    val eulerAngles: EulerAngles = EulerAngles(this, rotation.eulerAngles)

    fun lookAt(position: Vector3f) {
        rotation = Quaternion.angle((position - this.position).toVector3f().normalize(), Vector3f.forward)
    }

    private fun calculateRotationMatrix(): Matrix3f =
        Matrix3f.rotationMatrix(globalRotation)

    private fun calculateForward(): Vector3f = rotation.rotate(Vector3f.forward)
    private fun calculateRight(): Vector3f = rotation.rotate(Vector3f.right)
    private fun calculateUp(): Vector3f = rotation.rotate(Vector3f.up)
}

class EulerAngles(private val transform: Transform, angles: Vector3f) : Vector3f(angles.x, angles.y, angles.z) {
    override var x: Float = super.x
        set(value) {
            field = value
            updateTransform()
        }
    override var y: Float = super.y
        set(value) {
            field = value
            updateTransform()
        }
    override var z: Float = super.z
        set(value) {
            field = value
            updateTransform()
        }

    init {
        updateTransform()
    }

    private fun updateTransform() {
        transform.rotation = Quaternion.euler(x, y, z)
    }
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