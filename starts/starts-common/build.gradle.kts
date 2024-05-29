plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.cloud)
            implementation(projects.cache)
            implementation(projects.core.ui)
            implementation(projects.core.time)
            implementation(libs.kotlinx.serialization.core)
        }
    }
}
android {
    namespace = "com.markettwits.starts_common"
}