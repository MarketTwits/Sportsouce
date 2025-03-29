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
            implementation(projects.components.analytics.crashlytics)
            implementation(projects.components.auth.authService)
            implementation(projects.components.club.cloud)
            implementation(projects.components.core.cache)
            implementation(projects.components.bottombar)
            implementation(projects.components.core.ui)
            implementation(projects.components.core.time)
            implementation(projects.components.core.koin)
            implementation(projects.components.core.intent.api)
            implementation(projects.components.core.intent.impl)
            implementation(projects.components.core.errors)
            implementation(libs.compottie)
            implementation(libs.koin.core)
            implementation(libs.bundles.decompose.compose)
            implementation(libs.bundles.mviKotlin)
            implementation(compose.components.resources)
        }
    }
}