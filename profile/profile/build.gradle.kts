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
    implementation(projects.starts.startsCommon)
    implementation(libs.lottie)
    implementation("com.himanshoe:charty:2.0.0-alpha01")
    implementation("co.yml:ycharts:2.1.0")
    implementation(libs.koin.core)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.mviKotlin)
    implementation (libs.calendar)
}