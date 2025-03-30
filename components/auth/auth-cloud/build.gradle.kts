plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(projects.components.core.buildConfig)
            implementation(libs.kotlinx.serialization.json)
            implementation(projects.components.core.cloud)
        }
    }
}
android {
    namespace = "com.markettwits.auth.cloud"
}