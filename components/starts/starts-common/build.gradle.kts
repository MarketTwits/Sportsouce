plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.build.konfig.convension)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.core.cache)
            implementation(projects.components.core.ui)
            implementation(projects.components.core.time)
            implementation(projects.components.starts.startsCloud)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
android {
    namespace = "com.markettwits.starts_common"
}