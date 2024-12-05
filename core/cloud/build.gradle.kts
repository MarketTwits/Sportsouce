plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}
android{
    namespace = "com.markettwits.core.cloud"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.json)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
        }
    }
}