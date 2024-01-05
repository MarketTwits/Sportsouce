plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.change_password"

}

dependencies {
    api(project(":cloud"))
    implementation(project(":core-ui"))
    implementation(project(":auth"))
    implementation(libs.bundles.decompose.compose)
    implementation(libs.bundles.mviKotlin)
}