
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
//    implementation("androidx.compose.ui:ui-tooling-preview:1.6.1")
//    val composeBom = platform("androidx.compose:compose-bom:2024.02.02")
//    api(composeBom)
//
//    debugApi("androidx.compose.ui:ui-tooling")
//    api("androidx.compose.ui:ui-tooling-preview")
//

//    debugApi(libs.bundles.composeUiBundleDebug)
}