@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
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
    implementation(libs.koin.core)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.material3.html.text)
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.mviKotlin)
}