
plugins {
    alias(libs.plugins.android.library.compose.convention)
}

android {
    namespace = "com.markettwits.core_ui"
}

dependencies {
    implementation (libs.calendar)
    api(libs.bundles.composeUiBundle)
    api(libs.junit.ext.ktx)
    api(libs.onebone.toolbar)
    debugApi(libs.bundles.composeUiBundleDebug)
}