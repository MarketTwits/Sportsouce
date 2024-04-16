plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.start"
}

dependencies {
    implementation(projects.core.time)
    implementation(projects.analytics.crashlytics)
    implementation(projects.cloud)
    implementation(projects.core.ui)
    implementation(projects.coreKoin)
    implementation(projects.auth.authService)
    implementation(projects.start.startRegister)
    implementation(projects.start.startSupport)
    implementation(projects.cache)
    implementation(libs.bundles.mviKotlin)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.koin.core)
    implementation(libs.lazytable)
}