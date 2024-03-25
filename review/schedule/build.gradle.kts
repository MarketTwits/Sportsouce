plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.schedule"
}
dependencies {
    implementation(projects.cache)
    implementation(projects.cloud)
    implementation(projects.coreUi)
    implementation(projects.coreKoin)
    implementation(projects.start.start)
    implementation(projects.startsCommon)
    implementation(libs.koin.core)
    implementation("com.kizitonwose.calendar:compose:2.5.0")
    implementation(libs.bundles.decompose.compose)
    implementation(libs.bundles.mviKotlin)
}
