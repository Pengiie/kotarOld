rootProject.name = "kotaro"
include("core")
include("editor")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

}
include("sandbox")
