package dev.pengie.kotaro.assets

class Path(val fullPath: String) {
    val name: String
    var extension: String? = null
        private set
    val path: String

    init {
        val split = fullPath.split("/", "\\")
        val last = split[split.lastIndex]
        if(last.contains('.')) {
            val p = last.split('.')
            name = p[0]
            extension = p[1]
        } else name = last
        path = if(split.size == 1)
            "/"
        else
            split.subList(0, split.lastIndex - 1).reduce { s, t -> s + t }
    }

    fun hasExtension(): Boolean = extension != null
}