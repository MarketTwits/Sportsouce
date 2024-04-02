plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.sportsauce.root"
}

dependencies {
    implementation(project(":starts:starts"))
    implementation(project(":profile:profile"))
    implementation(project(":review:review"))
    implementation(project(":core-ui"))
    implementation(libs.koin.core)
    implementation(projects.coreKoin)
    implementation(libs.bundles.composeUiBundle)
    implementation(libs.bundles.decompose.compose)
}