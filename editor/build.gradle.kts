import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.21"
    id("org.jetbrains.compose") version "1.0.0-alpha3"
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation(project(":core"))
    implementation(compose.desktop.currentOs)

    val lwjglVersion = "3.2.3"
    val lwjgl3awtVersion = "0.1.9-SNAPSHOT"
    val lwjglNatives = when(org.gradle.internal.os.OperatingSystem.current()) {
        org.gradle.internal.os.OperatingSystem.WINDOWS -> "natives-windows"
        else -> throw Error("Unrecognized or unsupported Operating system. Please set \"lwjglNatives\" manually")
    }

    listOf("", "-glfw", "-opengl").forEach {
        implementation("org.lwjgl:lwjgl$it:$lwjglVersion")
        runtimeOnly("org.lwjgl:lwjgl$it:$lwjglVersion:$lwjglNatives")
    }
    implementation("org.lwjglx:lwjgl3-awt:$lwjgl3awtVersion")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Kotaro Editor"
            packageVersion = "1.0.0"
        }
    }
}