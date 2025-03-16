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
            api(libs.ktor.client.okhttp)
            api(libs.compose.ui.tooling.preview)
            api(libs.ktor.client.okhttp)
            api(libs.htmlconverter)
        }

        commonMain.dependencies {
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material3)
            api(compose.materialIconsExtended)
            api(compose.material3AdaptiveNavigationSuite)
            api(compose.components.uiToolingPreview)
            api(compose.ui)
            api(libs.kotlinx.collections.immutable)
            implementation(libs.kotlinx.datetime)
            implementation(projects.core.time)
            implementation(compose.components.resources)
            implementation("com.mohamedrejeb.richeditor:richeditor-compose:1.0.0-rc11")
            api(libs.ktor.core)
            api(libs.coil.compose)
            api(libs.coil.compose.core)
            api(libs.coil.mp)
            api(libs.coil.network)
        }
        
        jvmMain.dependencies {
            api(libs.htmlconverter)
            api(compose.animation)
            api(libs.compose.ui.tooling.preview)
        }

        jsMain.dependencies {
            api(libs.ktor.client.js)
        }
    }
}