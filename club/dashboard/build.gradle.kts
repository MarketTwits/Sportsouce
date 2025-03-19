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
            implementation(projects.analytics.crashlytics)
            implementation(projects.auth.authService)
            implementation(projects.club.cloud)
            implementation(projects.core.cache)
            implementation(projects.bottombar)
            implementation(projects.core.ui)
            implementation(projects.core.time)
            implementation(projects.core.koin)
            implementation(projects.intent.api)
            implementation(projects.intent.impl)
            implementation(projects.core.errors)
            implementation(libs.compottie)
            implementation(libs.koin.core)
            implementation(libs.bundles.decompose.compose)
            implementation(libs.bundles.mviKotlin)
            implementation(compose.components.resources)
        }
    }
}