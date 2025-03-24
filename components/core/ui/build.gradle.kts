plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}
android {
    namespace = "org.markettwits.core.core.ui"
}

kotlin {

    sourceSets {

        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }

        androidMain.dependencies {
            api(libs.compose.ui.tooling.preview)
            api(libs.ktor.client.okhttp)
            implementation(libs.compose.activity)
        }

        commonMain.dependencies {
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material3)
            api(compose.materialIconsExtended)
            api(compose.material3AdaptiveNavigationSuite)
            api(compose.components.uiToolingPreview)
            api(compose.ui)
            api(libs.coil.compose)
            api(libs.coil.network)
            api(libs.coil.mp)
            api(libs.richeditor)
            api(libs.composeMaterial3WindowSize)
            api(libs.composeMaterial3Adaptive)
            implementation(compose.components.resources)
            implementation(libs.kotlinx.serialization.json)
            implementation(projects.components.core.time)
        }
        
        jvmMain.dependencies {
            api(compose.animation)
            api(libs.compose.ui.tooling.preview)
        }
        jsMain.dependencies {
            api(libs.ktor.client.js)
        }
    }
}