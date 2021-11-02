package dev.pengie.kotaro.graphics

import dev.pengie.kotaro.types.Disposable

abstract class Model : Disposable {
 abstract fun create(mesh: Mesh)
}

expect object ModelFactory {
 fun createModel() : Model
}