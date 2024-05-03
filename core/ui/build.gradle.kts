plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose)
}
android {
    namespace = "org.markettwits.core.core.ui"

    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toString().toInt()
        targetSdk = libs.versions.targetSdk.get().toString().toInt()
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
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
            api("io.ktor:ktor-client-okhttp:2.3.7")
        }
        commonMain.dependencies {
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material3)
            api(compose.materialIconsExtended)
            api(compose.ui)
            implementation(compose.components.resources)
            api(compose.components.uiToolingPreview)
            api("dev.materii.pullrefresh:pullrefresh:1.4.0-beta02")
            api("io.coil-kt.coil3:coil-compose:3.0.0-alpha06")
            api("io.coil-kt.coil3:coil-network-ktor:3.0.0-alpha06")
            api("be.digitalia.compose.htmlconverter:htmlconverter:0.9.5")
            api(libs.kotlinx.collections.immutable)
        }
        jvmMain.dependencies {
            api(compose.animation)
            api(libs.compose.ui.tooling.preview)
        }
    }
}