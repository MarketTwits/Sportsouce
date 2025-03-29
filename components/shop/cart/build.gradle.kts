plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}

android.namespace = "com.markettwits.shop.cart"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.bundles.decompose.compose)
            implementation(libs.bundles.mviKotlin)
            implementation(libs.koin.core)
            implementation(projects.components.core.intent.composable)
            implementation(projects.components.shop.domain)
            implementation(projects.components.core.cache)
            implementation(projects.components.core.ui)
            implementation(projects.components.core.koin)
            implementation(projects.components.shop.cloud)
            implementation(projects.components.auth.authService)
        }
    }
}