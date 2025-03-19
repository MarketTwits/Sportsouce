plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.build.konfig.convension)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.cache)
            implementation(projects.core.ui)
            implementation(projects.core.time)
            implementation(projects.starts.startsCloud)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
android {
    namespace = "com.markettwits.starts_common"
}