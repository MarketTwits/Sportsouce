plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}

android.namespace = "com.markettwits.shop.order"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.bundles.mviKotlin)
            implementation(projects.components.core.errors)
            implementation(projects.components.core.decompose)
            implementation(projects.components.shop.cloud)
            implementation(projects.components.shop.cart)
            implementation(projects.components.core.ui)
            implementation(projects.components.shop.domain)
            implementation(projects.components.core.koin)
            implementation(projects.components.core.intent.api)
            implementation(projects.components.core.intent.impl)
            implementation(projects.components.auth.authService)
        }
    }
}
