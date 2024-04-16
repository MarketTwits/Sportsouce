plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.popular"
}
dependencies {
    implementation(projects.core.time)
    implementation(projects.cloud)
    implementation(projects.core.ui)
    implementation(projects.coreKoin)
    implementation(libs.koin.core)
    implementation(libs.bundles.decompose.compose)
    implementation(projects.starts.startsCommon)
    implementation(projects.start.start)
    implementation(libs.bundles.mviKotlin)
}
