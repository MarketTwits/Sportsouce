plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.change_password"

}

dependencies {
    implementation(projects.cloud)
    implementation(projects.auth.authService)
    implementation(projects.coreUi)
    implementation(projects.coreKoin)
    implementation(libs.koin.core)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.bundles.mviKotlin)
}