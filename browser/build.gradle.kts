plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    js(IR) {
        useEsModules()
        nodejs()
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    sourceSets.jsMain.dependencies {
        implementation(projects.core.theme)
        implementation(projects.core.time)
        implementation(projects.core.ui)
        implementation(projects.root)
        implementation(projects.core.cache)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.koin.core)
        implementation(projects.core.koin)

        implementation(project.dependencies.enforcedPlatform(libs.jetbrains.kotlinWrappers.kotlinWrappersBom))
        implementation("org.jetbrains.kotlin-wrappers:kotlin-browser")
    }
}