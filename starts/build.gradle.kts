plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)

}

android {
    namespace = "com.markettwits.starts"
}
dependencies {
    api(project(":cloud"))
    implementation(project(":start"))
    implementation(project(":core-ui"))
    implementation(libs.bundles.decompose.compose)
    implementation (libs.material3.html.text)
    implementation(libs.kotlinx.datetime)
}