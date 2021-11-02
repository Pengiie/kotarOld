import org.gradle.internal.os.OperatingSystem

plugins {
    id("maven-publish")
    kotlin("multiplatform") version "1.5.30"
}

version = "1.0-SNAPSHOT"

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
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                val lwjglVersion = "3.2.3"
                val lwjglNatives = when(OperatingSystem.current()) {
                    OperatingSystem.WINDOWS -> "natives-windows"
                    else -> throw Error("Unrecognized or unsupported Operating system. Please set \"lwjglNatives\" manually")
                }

                listOf("", "-glfw", "-opengl").forEach {
                    implementation("org.lwjgl:lwjgl$it:$lwjglVersion")
                    runtimeOnly("org.lwjgl:lwjgl$it:$lwjglVersion:$lwjglNatives")
                }
            }
        }
    }

    publishing {
        repositories {
            maven {
                url = uri(
                    if(version.toString().endsWith("SNAPSHOT"))
                        ""
                    else
                        ""
                )

            }
        }
        publications {

        }
    }
}