plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
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
            implementation(projects.bottombar)
            implementation(projects.core.ui)
            implementation(projects.core.time)
            implementation(projects.coreKoin)
            implementation(projects.intent.api)
            implementation(projects.intent.impl)
            implementation(libs.compottie)
            implementation(libs.koin.core)
            implementation(libs.bundles.decompose.compose)
            implementation(libs.bundles.mviKotlin)
            implementation(libs.lazytable)
            implementation(compose.components.resources)
        }
    }
}