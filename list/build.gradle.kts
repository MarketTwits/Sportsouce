plugins {
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
            implementation(projects.cloud)
            implementation(projects.coreUi)
            implementation(projects.randomUser)
            implementation(libs.kstore.file)
            implementation(libs.kstore)

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
            implementation(libs.kstore.file)
            implementation(libs.kotlinx.coroutines.swing)
        }
        androidMain.dependencies {
            implementation(libs.kstore.file)
        }

    }
}

android {
    namespace = libs.versions.namespace.get()
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34
    }
}