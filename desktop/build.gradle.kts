import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}
tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.markettwits.sportsouce.app.MainKt"
    }
}

kotlin {

jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "20"
        }
        withJava()
    }

    jvmToolchain(20)

    sourceSets.jvmMain.dependencies {
        implementation(compose.desktop.currentOs)
        implementation(projects.core.ui)
        implementation(libs.koin.core)
        implementation(projects.coreKoin)
        implementation(compose.desktop.common)
        implementation(projects.root)
        implementation(projects.core.theme)
        implementation(libs.bundles.decompose.compose)
        implementation(projects.cache)
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.4")
        implementation("com.badoo.reaktive:reaktive:1.2.3")
        implementation("com.badoo.reaktive:coroutines-interop:1.2.3")
    }
}

compose.desktop {
    application {

        mainClass = "com.markettwits.sportsouce.app.MainKt"

        nativeDistributions {
            packageName = "Спорт Союз"
            // packageVersion = libs.versions.versionName.get()
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
