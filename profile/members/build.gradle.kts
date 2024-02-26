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
    implementation(projects.auth)
    implementation(projects.coreKoin)
    implementation(libs.koin.core)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.bundles.mviKotlin)
}