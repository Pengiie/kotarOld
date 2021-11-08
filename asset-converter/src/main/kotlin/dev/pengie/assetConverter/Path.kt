package dev.pengie.assetConverter

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
            name = p[p.size - 2]
            extension = p[p.size - 1]
        } else name = last
        path = if(split.size == 1)
            "/"
        else
            split.subList(0, split.lastIndex - 1).reduce { s, t -> s + t }
    }

    fun isDirectory(): Boolean = extension == null
}