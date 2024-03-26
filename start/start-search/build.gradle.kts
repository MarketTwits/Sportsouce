plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.start_search"
}

dependencies {
    implementation(projects.cache)
    implementation(projects.cloud)
    implementation(projects.coreUi)
    implementation(projects.coreKoin)
    implementation(projects.start.start)
    implementation(projects.start.startFilter)
    implementation(projects.starts.startsCommon)
    implementation(libs.bundles.mviKotlin)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.koin.core)
    implementation(libs.material3.html.text)
}