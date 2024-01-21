
plugins {
    alias(libs.plugins.android.library.compose.convention)
}

android {
    namespace = "com.markettwits.core_ui"
}

dependencies {
    api(libs.bundles.composeUiBundle)
    api(libs.junit.ext.ktx)
    debugApi(libs.bundles.composeUiBundleDebug)
}