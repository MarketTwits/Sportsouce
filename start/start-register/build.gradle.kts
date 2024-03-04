plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.start_register"
}

dependencies {
    implementation(projects.cloud)
    implementation(projects.coreUi)
    implementation(projects.coreKoin)
    implementation(projects.auth.authService)
    implementation(projects.teamsCity)
    implementation(libs.bundles.mviKotlin)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.koin.core)
    implementation(libs.calendar)
    implementation(libs.material3.html.text)
}