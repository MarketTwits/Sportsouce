plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android.namespace = "com.markettwits.inappnotification.api"

dependencies {
    implementation(libs.compose.ui.tooling)
    implementation(libs.kotlinx.serialization.json)
}
