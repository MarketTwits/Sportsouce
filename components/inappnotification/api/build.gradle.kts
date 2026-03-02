plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}


kotlin {

    android {
        namespace = "com.markettwits.inappnotification.api"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.ui)
        }
    }
}
