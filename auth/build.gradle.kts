plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.realm)
}

android {
    namespace = "com.markettwits.auth"
}
dependencies {
    api(project(":cloud"))
    implementation(project(":core-ui"))
    implementation(libs.bundles.decompose.compose)
    implementation (libs.material3.html.text)
    implementation(libs.kotlinx.datetime)
    implementation(libs.realm)
}