plugins {
    alias(libs.plugins.android.library.convention)
    alias(libs.plugins.kotlin.serialization)
}

android.namespace = "com.markettwits.selfupdater.thirdparty.github"

dependencies {
    implementation(projects.selfupdater.thirdparty.api)
    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.coreCloud)
    implementation(libs.ktor.core)
    implementation(libs.ktor.client.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.koin.core)

}
