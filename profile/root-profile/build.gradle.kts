plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)

}

android {
    namespace = "com.markettwits.root_profile"
}
dependencies {
    implementation(projects.profile.unauthorized)
    implementation(projects.profile.authorized.profile)
    implementation(projects.auth.authFlow)
    implementation(projects.coreKoin)
    implementation(libs.koin.core)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.bundles.composeUiBundle)
    implementation(libs.bundles.mviKotlin)
}