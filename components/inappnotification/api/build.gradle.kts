plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android.namespace = "com.markettwits.inappnotification.api"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.ui)
        }
    }
}