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
    implementation(projects.coreUi)
    implementation(projects.coreKoin)
    implementation(libs.koin.core)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.material3.html.text)
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.mviKotlin)
}