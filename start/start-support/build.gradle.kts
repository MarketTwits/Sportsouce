plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.start_support"
}

dependencies {
    implementation(projects.cloud)
    implementation(projects.coreUi)
    implementation(projects.coreKoin)
    implementation(libs.bundles.mviKotlin)
    implementation(libs.decompose)
    implementation(libs.koin.core)
}