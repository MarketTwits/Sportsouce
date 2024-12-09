
plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.build.konfig.convension)
    alias(libs.plugins.kotlin.serialization)
}
android{
    namespace = "com.markettwits.start.start_cloud"
}

kotlin {
    sourceSets{
        commonMain.dependencies {
            implementation(projects.core.log)
            api(libs.ktor.client.json)
            api(libs.ktor.client.okhttp)
            implementation(projects.core.koin)
            implementation(libs.ktor.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(projects.core.cloud)
        }
    }
}


