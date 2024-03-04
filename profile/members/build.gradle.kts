plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.members"
}

dependencies {
    implementation(projects.cloud)
    implementation(projects.coreUi)
    implementation(projects.auth.authService)
    implementation(projects.coreKoin)
    implementation(projects.teamsCity)
    implementation(libs.koin.core)
    implementation(libs.calendar)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.bundles.mviKotlin)
}