package dev.pengie.kotaro

class DesktopConfig : ApplicationConfig {
    override var platform: Platform = when {
        System.getProperty("os.name")!!.startsWith("Windows") -> Platform.WINDOWS
        else -> Platform.UNKNOWN
    }

    override var width = 1280
    override var height = 720
    var resizable = false
    var title = "Kotaro Engine Game"

    fun width(width: Int) = apply { this.width = width }
    fun height(height: Int) = apply { this.height = height }
    fun resizable(resizable: Boolean) = apply { this.resizable = resizable }
    fun title(title: String) = apply { this.title = title }
}

actual object ApplicationConfigFactory {
    actual fun getConfig(): ApplicationConfig = DesktopConfig()
}