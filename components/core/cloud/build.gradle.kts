plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {

    android {
        namespace = "com.markettwits.core.cloud"
    }

    sourceSets {

        jvmMain.dependencies{
            api(libs.ktor.client.okhttp)
        }
        androidMain.dependencies{
            api(libs.ktor.client.okhttp)
        }
        jsMain.dependencies {
            api(libs.ktor.client.js)
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
