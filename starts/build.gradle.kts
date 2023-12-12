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
    api(libs.bundles.composeUiBundle)
    api(libs.junit.ext.ktx)
    debugApi(libs.bundles.composeUiBundleDebug)
    implementation(libs.decompose)
    //implementation(libs.decompose.compose.jetbrains)
    implementation(libs.decompose.compose.extension)
    implementation ("de.charlex.compose.material3:material3-html-text:2.0.0-beta01")
    implementation(libs.decompose.android)
    implementation(libs.kotlinx.datetime)
    api("androidx.appcompat:appcompat:1.6.1")
}