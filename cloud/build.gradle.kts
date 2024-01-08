plugins {
    alias(libs.plugins.android.library.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.cloud"
}

dependencies {
    api(libs.ktor.core)
    api(libs.ktor.client.json)
    api(libs.ktor.client.logging)
    api(libs.ktor.client.content.negotiation)
    api(libs.ktor.client.okhttp)
    implementation(libs.kotlinx.serialization.json)
  //  api(libs.kotlinx.serialization.json)
}