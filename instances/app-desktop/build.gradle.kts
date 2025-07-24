import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.reload.ComposeHotRun

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose.hot-reload")
}

val desktopMainPath = "com.markettwits.sportsouce.app.desktop.MainKt"

tasks {
    withType<Jar> {
        manifest {
            attributes["Main-Class"] = desktopMainPath
        }
    }
    withType<ComposeHotRun>().configureEach {
        mainClass.set(desktopMainPath)
    }
}

kotlin {
    jvm()
    jvmToolchain(21)

    sourceSets.jvmMain.dependencies {
        implementation(compose.desktop.currentOs)
        implementation(projects.components.core.ui)
        implementation(libs.koin.core)
        implementation(projects.components.core.koin)
        implementation(compose.desktop.common)
        implementation(projects.components.root)
        implementation(projects.components.core.theme)
        implementation(libs.bundles.decompose.compose)
        implementation(projects.components.core.cache)
        implementation(libs.kotlinx.coroutines.swing)
        implementation(libs.reaktive)
        implementation(libs.coroutines.interop)
    }
}

compose.desktop {
    application {

        mainClass = "com.markettwits.sportsouce.app.desktop.MainKt"

        nativeDistributions {

            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)

            packageName = "Sportsauce"
            description = "Sportsauce Desktop Application"
            copyright = "© 2024 Sportsauce."
            vendor = "MarketTwits"

            linux {
                iconFile.set(project.file("desktopAppIcons/LinuxSportSauceIcon.png"))
            }
            windows {
                iconFile.set(project.file("desktopAppIcons/WindowsSportSauceIcon.ico"))
            }
            macOS {
                iconFile.set(project.file("desktopAppIcons/MacSportSauceIcon.icns"))
                bundleID = "com.markettwits.sibersspace.desktopApp"
            }
            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
        }

        buildTypes.release.proguard {
            configurationFiles.from("compose-desktop.pro")
            obfuscate.set(false)
            optimize.set(false)
            version.set("7.5.0")
        }
    }
}
