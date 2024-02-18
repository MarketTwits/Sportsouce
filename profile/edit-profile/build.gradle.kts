plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)

}

android {
    namespace = "com.markettwits.edit_profile"
}
dependencies {
    api(project(":cloud"))
    implementation(project(":auth"))
    implementation(project(":core-ui"))
    implementation(project(":core-koin"))
    implementation("io.github.mr0xf00:easycrop:0.1.1")
    implementation(projects.profile.changePassword)
    implementation(libs.koin.core)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.mviKotlin)
    implementation (libs.calendar)
}