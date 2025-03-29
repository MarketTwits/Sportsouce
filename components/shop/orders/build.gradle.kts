plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}

android.namespace = "com.markettwits.shop.orders"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.bundles.mviKotlin)
            implementation(libs.decompose)
            implementation(projects.components.core.ui)
            implementation(projects.components.core.errors)
            implementation(projects.components.core.time)
            implementation(projects.components.core.koin)
            implementation(projects.components.shop.cloud)
            implementation(projects.components.shop.domain)
            implementation(projects.components.shop.order)
            implementation(projects.components.analytics.crashlytics)
            implementation(projects.components.auth.authService)
            implementation(projects.components.core.intent.composable)
        }
    }
}
