plugins {
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "org.markettwits.core.decompose"
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            api(libs.bundles.decompose.compose)
        }
    }
}