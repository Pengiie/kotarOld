package dev.pengie.kotaro.utils

import dev.pengie.kotaro.graphics.Texture
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
fun KClass<*>.toAny(): KClass<Any> = this as KClass<Any>