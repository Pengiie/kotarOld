package dev.pengie.kotaro.utils

import dev.pengie.kotaro.types.Disposable

object Disposer {
    operator fun invoke(vararg disposables: Disposable) = disposables.forEach(Disposable::dispose)
}