plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)

}

android {
    namespace = "com.markettwits.edit_profile"
}
dependencies {
    implementation(projects.cloud)
    implementation(projects.auth.authService)
    implementation(projects.coreUi)
    implementation(projects.coreKoin)
    implementation(projects.teamsCity)
    implementation("io.github.mr0xf00:easycrop:0.1.1")
    implementation(libs.koin.core)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.mviKotlin)
    implementation(libs.calendar)
}