plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
}
kotlin {

    android {
        namespace = "com.markettwits.profile.cloud"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(projects.components.core.cloud)
        }
    }
}
