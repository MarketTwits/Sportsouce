plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.unauthorized"
}

dependencies {
    implementation(projects.cloud)
    implementation(projects.auth.authService)
    implementation(projects.coreUi)
    implementation(libs.koin.core)
    implementation(libs.lottie)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.bundles.mviKotlin)
}