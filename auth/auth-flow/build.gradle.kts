plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.auth_flow"
}
dependencies {
    implementation(projects.analytics.crashlytics)
    implementation(projects.auth.authService)
    implementation(projects.cloud)
    implementation(projects.core.ui)
    implementation(projects.coreKoin)
    implementation(libs.koin.core)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.bundles.mviKotlin)
    implementation(projects.core.time)
}