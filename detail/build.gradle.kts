plugins {
//    alias(libs.plugins.android.library.compose.convention)
//    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)
}
kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    jvm()

    sourceSets {
        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }
        commonMain.dependencies {
            implementation(projects.randomUser)
            implementation(projects.cloud)
            implementation(projects.coreUi)

            implementation(libs.bundles.mviKotlin)
            implementation(libs.decompose)
            implementation(libs.pullrefresh)
            with(compose) {
                implementation(runtime)
                implementation(foundation)
                implementation(material)
                implementation(material3)
                implementation(materialIconsExtended)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(components.resources)
            }
            implementation(libs.composeImageLoader)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(compose.preview)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.common)
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

android {
    namespace = "org.markettwits.shift"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34
    }
}