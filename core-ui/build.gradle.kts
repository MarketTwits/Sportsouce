
plugins {
    alias(libs.plugins.android.library.compose.convention)
}

android {
    namespace = "com.markettwits.core_ui"
}

dependencies {
    implementation(projects.cache)
    implementation (libs.calendar)
    implementation(libs.libphonenumber)
    implementation(libs.zoomable)
    api(libs.bundles.composeUiBundle)
    api(libs.junit.ext.ktx)
    api(libs.onebone.toolbar)

    api(libs.compose.ui.tooling.preview)
    debugApi(libs.compose.ui.tooling)
}