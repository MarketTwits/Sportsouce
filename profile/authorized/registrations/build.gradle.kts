plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.registrations"

}

dependencies {
    implementation(project(":cloud"))
    implementation(project(":auth:auth-service"))
    implementation(project(":core-ui"))
    implementation(project(":start:start"))
    implementation(project(":core-koin"))
    implementation(projects.core.time)
    implementation(libs.koin.core)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.material3.html.text)
    implementation(libs.bundles.mviKotlin)
}