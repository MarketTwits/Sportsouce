plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    js(IR) {
        useEsModules()
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    sourceSets.jsMain.dependencies {
        implementation(projects.components.core.theme)
        implementation(projects.components.core.time)
        implementation(projects.components.core.ui)
        implementation(projects.components.root)
        implementation(projects.components.core.cache)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.kotlinx.datetime.ext)
        implementation(libs.koin.core)
        implementation(projects.components.core.koin)
        implementation(project.dependencies.enforcedPlatform(libs.jetbrains.kotlinWrappers.kotlinWrappersBom))
        implementation(libs.kotlin.browser)
    }
}