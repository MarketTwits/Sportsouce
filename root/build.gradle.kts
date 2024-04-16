plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.sportsauce.root"
}

dependencies {
    implementation(projects.starts.starts)
    implementation(projects.profile.rootProfile)
    implementation(projects.review.review)
    implementation(projects.core.ui)
    implementation(libs.koin.core)
    implementation(projects.coreKoin)
    implementation(libs.bundles.composeUiBundle)
    implementation(libs.bundles.decompose.compose)
}