plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.club.dashboard"
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.club.cloud)
            implementation(projects.cache)
            implementation(libs.compottie)
            implementation(projects.bottombar)
            implementation(projects.core.ui)
            implementation(projects.core.time)
            implementation(projects.coreKoin)
            implementation(libs.koin.core)
            implementation(libs.bundles.decompose.compose)
            implementation(libs.bundles.mviKotlin)
            implementation(libs.lazytable)
            implementation(compose.components.resources)
        }
    }
}