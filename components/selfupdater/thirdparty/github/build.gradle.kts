plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}


kotlin {

    android {
        namespace = "com.markettwits.selfupdater.thirdparty.github"
    }

    jvm()
    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.selfupdater.thirdparty.api)
            implementation(libs.kotlinx.coroutines.core)
            implementation(projects.components.core.cloud)
            implementation(libs.ktor.core)
            implementation(libs.ktor.client.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(projects.components.core.buildConfig)
        }
    }
}
