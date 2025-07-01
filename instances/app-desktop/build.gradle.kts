import org.gradle.kotlin.dsl.withType
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.reload.ComposeHotRun

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose.hot-reload")
}

val desktopMainPath = "com.markettwits.sportsouce.app.desktop.MainKt"

//tasks.withType<Jar> {
//    manifest {
//        attributes["Main-Class"] = desktopMainPath
//    }
//}
//tasks.withType<ComposeHotRun>().configureEach {
//    mainClass.set(desktopMainPath)
//}

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
            packageName = "Спорт Союз"
            description = "Sportsauce Desktop Application"
            copyright = "© 2024 My Name. All rights reserved."
            vendor = "MarketTwits"
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
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
