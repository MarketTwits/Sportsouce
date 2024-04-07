plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.start"
}

dependencies {
    implementation(projects.analytics.crashlytics)
    implementation(projects.cloud)
    implementation(projects.coreUi)
    implementation(projects.coreKoin)
    implementation(projects.auth.authService)
    implementation(projects.start.startRegister)
    implementation(projects.start.startSupport)
    implementation(projects.cache)
    implementation(libs.bundles.mviKotlin)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.koin.core)
    implementation(libs.material3.html.text)
    implementation(libs.lazytable)
}