plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.build.konfig.convension)
}
android{
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
            implementation(libs.ktor.client.json)
            implementation(libs.ktor.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
        }
    }
}