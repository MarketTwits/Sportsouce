import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose.hot-reload")
}

val desktopMainClass = "com.markettwits.sportsouce.app.desktop.MainKt"
val desktopPackageVersion = libs.versions.versionName

tasks {
    withType<Jar> {
        manifest {
            attributes["Main-Class"] = desktopMainClass
        }
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
        implementation(projects.components.deeplink.api)
        implementation(projects.components.root)
        implementation(projects.components.core.theme)
        implementation(libs.bundles.decompose.compose)
        implementation(projects.components.core.cache)
        implementation(libs.kotlinx.coroutines.swing)
        implementation(libs.ktor.client.okhttp)
        implementation(libs.reaktive)
        implementation(libs.coroutines.interop)
    }
}

compose.desktop {
    application {

        mainClass = desktopMainClass

        nativeDistributions {

            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)

            packageName = "SportSauce"
            packageVersion = desktopPackageVersion.get()
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
            appResourcesRootDir.set(project.layout.projectDirectory.dir("src/jvmMain/resources"))
        }

        buildTypes.release.proguard {
            configurationFiles.from(project.file("compose-desktop.pro"))
        }
    }
}
