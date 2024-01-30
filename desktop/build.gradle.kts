import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
}

group = "com.markettwits"
version = "1.0-SNAPSHOT"

kotlin {
    jvm {
        jvmToolchain(8)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":root"))
                implementation(project(":list"))
                implementation(libs.decompose)
                implementation(libs.mvikotlin)
                implementation(compose.desktop.currentOs)
                implementation(libs.harawata.appdirs)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Msi, TargetFormat.Exe)
            packageName = "desktop"
            packageVersion = "1.0.0"
        }
    }
}
