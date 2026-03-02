plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}

kotlin {

    android {
        namespace = "org.markettwits.core.capturable"
    }

    sourceSets {

        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            api(libs.ktor.client.okhttp)
            implementation(libs.compose.activity)
        }

        commonMain.dependencies {
            implementation(projects.components.core.ui)
//            api(libs.compose.runtime)
//            api(libs.compose.foundation)
//            api(libs.compose.material3)
//            api(libs.compose.material.icons.extended)
//            api(libs.compose.components.ui.tooling.preview)
//            api(libs.compose.ui)
//            api(libs.coil.compose)
//            api(libs.coil.network)
//            api(libs.coil.mp)
//            api(libs.richeditor)
//            api(libs.composeMaterial3WindowSize)
//            api(libs.composeMaterial3Adaptive)
//            implementation(libs.compose.components.resources)
//            implementation(libs.kotlinx.serialization.json)
//            implementation(projects.components.core.time)
        }

//        jvmMain.dependencies {
//            api(libs.compose.animation)
//            api(libs.compose.ui.tooling.preview)
//        }
//
//        jsMain.dependencies {
//            api(libs.ktor.client.js)
//        }
    }
}
