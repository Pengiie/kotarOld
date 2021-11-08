plugins {
    kotlin("multiplatform") version "1.5.30"
}

repositories {
    mavenCentral()
}

version = "1.0-SNAPSHOT"

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(project(":kotaro"))
            }
        }
    }
}