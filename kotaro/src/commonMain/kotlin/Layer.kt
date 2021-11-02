package dev.pengie.kotaro

import dev.pengie.kotaro.types.Disposable

interface Layer : Disposable {
    fun init()
    fun update()
    fun render();
}