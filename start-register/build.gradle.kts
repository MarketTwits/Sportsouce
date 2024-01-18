plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.start_register"

}

dependencies {
    api(project(":cloud"))
    implementation(project(":core-ui"))
    implementation(project(":core-koin"))
    implementation(project(":auth"))
    implementation(libs.bundles.mviKotlin)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.koin.core)
    implementation (libs.calendar)
    implementation(libs.material3.html.text)
}