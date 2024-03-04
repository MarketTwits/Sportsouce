plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)

}

android {
    namespace = "com.markettwits.profile"
}
dependencies {
    implementation(projects.start.start)
    implementation(projects.cloud)
    implementation(projects.auth.authService)
    implementation(projects.auth.authFlow)
    implementation(projects.coreUi)
    implementation(projects.coreKoin)
    implementation(projects.profile.editProfile)
    implementation(projects.profile.registrations)
    implementation(projects.profile.members)
    implementation(projects.cache)
    implementation(projects.startsCommon)
    implementation(libs.lottie)
    implementation(libs.koin.core)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.mviKotlin)
    implementation (libs.calendar)
}