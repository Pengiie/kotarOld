plugins {
    kotlin("jvm") version "1.5.31"
}

version = "1.0-SNAPSHOT"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    implementation(project(":kotaro"))
}