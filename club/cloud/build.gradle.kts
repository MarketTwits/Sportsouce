plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.build.konfig.convension)
}
android {
    namespace = "com.markettwits.club.cloud"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.ktor.client.json)
            api(libs.ktor.client.okhttp)
            implementation(projects.core.koin)
            implementation(projects.core.cloud)
            implementation(libs.ktor.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
        }
    }
}