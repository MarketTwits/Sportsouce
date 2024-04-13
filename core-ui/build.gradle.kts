
plugins {
    alias(libs.plugins.android.library.compose.convention)
}

android {
    namespace = "com.markettwits.core_ui"
}

dependencies {
    implementation(libs.zoomable)
    api(libs.kotlinx.collections.immutable)
    api(libs.compose.ui.tooling.preview)
    api(libs.bundles.composeUiBundle)
    debugApi(libs.junit.ext.ktx)
    debugApi(libs.compose.ui.tooling)
}