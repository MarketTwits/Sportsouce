plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
}
kotlin {

    android {
        namespace = "com.markettwits.auth.cloud"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(projects.components.core.buildConfig)
            implementation(libs.kotlinx.serialization.json)
            implementation(projects.components.core.cloud)
        }
    }
}
