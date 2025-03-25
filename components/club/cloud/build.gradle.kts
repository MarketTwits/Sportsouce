plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.club.cloud"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.ktor.client.json)
            implementation(projects.components.core.koin)
            implementation(projects.components.core.cloud)
            implementation(libs.ktor.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
        }
    }
}