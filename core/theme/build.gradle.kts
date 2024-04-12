plugins {
    alias(libs.plugins.android.library.compose.convention)
}

android {
    namespace = "com.markettwits.theme"
}

dependencies {
    implementation(libs.decompose)
    implementation(libs.koin.core)
    implementation(projects.coreKoin)
    implementation(projects.coreUi)
    implementation(libs.bundles.composeUiBundle)
    implementation(libs.compose.ui.tooling)
    implementation(projects.settings.api)
}