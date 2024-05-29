import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}
kotlin {
    jvm()
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
            version = libs.versions.versionName.get()
            description = "Sportsauce Desktop Application"
            copyright = "© 2024 My Name. All rights reserved."
            vendor = "MarketTwits"
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
        }
    }
}

