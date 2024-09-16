plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.jvm.convention)
}

dependencies {
    api(libs.ktor.client.json)
    api(libs.ktor.client.okhttp)
    implementation(libs.ktor.core)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.koin.core)
    implementation(projects.coreKoin)
    implementation(projects.coreCloud)
}