
plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.core_ui"
}

dependencies {
    implementation (libs.calendar)
    implementation(libs.kotlinx.serialization.core)
    api(libs.bundles.composeUiBundle)
    api(libs.junit.ext.ktx)
    debugApi(libs.bundles.composeUiBundleDebug)
}