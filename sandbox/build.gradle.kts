plugins {
    kotlin("jvm") version "1.5.31"
}

version = "1.0-SNAPSHOT"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":kotaro"))
}