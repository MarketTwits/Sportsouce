plugins {
    alias(libs.plugins.kotlin.jvm.convention)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(project(":core-koin"))
    api(libs.ktor.client.json)
    api(libs.ktor.client.okhttp)
    implementation(libs.ktor.core)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.koin.core)
}