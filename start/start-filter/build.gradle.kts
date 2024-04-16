plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.start_filter"
}
dependencies {
    implementation(projects.cloud)
    implementation(projects.core.ui)
    implementation(projects.coreKoin)
    implementation(projects.start.start)
    implementation(projects.starts.startsCommon)
    implementation(projects.cache)
    implementation(libs.koin.core)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.bundles.mviKotlin)
}
