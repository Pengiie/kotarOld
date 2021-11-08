rootProject.name = "kotaro-root"
include("kotaro")
include("editor")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

}
include("sandbox")
include("asset-converter")
