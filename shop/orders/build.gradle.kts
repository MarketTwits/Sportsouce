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
            implementation(projects.core.ui)
            implementation(projects.core.errors)
            implementation(projects.core.time)
            implementation(projects.core.koin)
            implementation(projects.shop.cloud)
            implementation(projects.shop.domain)
            implementation(projects.shop.order)
            implementation(projects.auth.authService)
            implementation(projects.intent.composable)
        }
    }
}
