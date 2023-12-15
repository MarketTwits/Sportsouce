plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.start"

}

dependencies {
    api(project(":cloud"))
    implementation(project(":core-ui"))
    implementation(libs.bundles.decompose.compose)
//    implementation ("de.charlex.compose.material3:material3-html-text:2.0.0-beta01")
}