import org.gradle.internal.os.OperatingSystem

plugins {
    kotlin("multiplatform") version "1.5.30"
}

kotlin {

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }

    sourceSets {
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                val lwjglVersion = "3.2.3"
                val lwjglNatives = when(org.gradle.internal.os.OperatingSystem.current()) {
                    org.gradle.internal.os.OperatingSystem.WINDOWS -> "natives-windows"
                    else -> throw Error("Unrecognized or unsupported Operating system. Please set \"lwjglNatives\" manually")
                }

                listOf("", "-glfw", "-opengl").forEach {
                    implementation("org.lwjgl:lwjgl$it:$lwjglVersion")
                    runtimeOnly("org.lwjgl:lwjgl$it:$lwjglVersion:$lwjglNatives")
                }
            }
        }
    }
}