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
    implementation(project(":auth"))
    implementation(libs.bundles.decompose.compose)
    implementation(libs.material3.html.text)
    implementation ("io.github.oleksandrbalan:lazytable:1.6.0")
}