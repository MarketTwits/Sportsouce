plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.core.cloud"
}

kotlin {
    sourceSets {

        jvmMain.dependencies{
            api(libs.ktor.client.okhttp)
        }
        androidMain.dependencies{
            api(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            api(libs.ktor.client.json)
            api(libs.ktor.core)
            api(libs.ktor.client.logging)
            api(libs.ktor.client.content.negotiation)
            api(projects.components.core.buildConfig)
        }
    }
}