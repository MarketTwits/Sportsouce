plugins {
    // alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose)
}

android.namespace = "com.markettwits.inappnotification.api"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.ui)
        }
    }
}