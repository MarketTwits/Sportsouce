plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)

}

android {
    namespace = "com.markettwits.profile"
}
dependencies {
    implementation(project(":cloud"))
    implementation(project(":auth"))
    implementation(project(":core-ui"))
    implementation(project(":core-koin"))
    implementation(project(":profile:edit-profile"))
    implementation(project(":profile:change-password"))
    implementation(project(":profile:registrations"))
    implementation(projects.startsCommon)
    implementation(libs.lottie)
    implementation(libs.koin.core)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.mviKotlin)
    implementation (libs.calendar)
}