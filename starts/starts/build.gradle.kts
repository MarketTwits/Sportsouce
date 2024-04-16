plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.starts"
}
dependencies {
    implementation(projects.core.time)
    implementation(projects.cache)
    implementation(projects.cloud)
    implementation(projects.coreKoin)
    implementation(projects.start.start)
    implementation(projects.start.startSearch)
    implementation(projects.core.ui)
    implementation(projects.starts.startsCommon)
    implementation(libs.koin.core)
    implementation(libs.bundles.decompose.compose)
}