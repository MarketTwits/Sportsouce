plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}

android.namespace = "com.markettwits.shop.item"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.decompose.compose)
            implementation(libs.koin.core)
            implementation(libs.bundles.mviKotlin)
            implementation(projects.components.shop.domain)
            implementation(projects.components.shop.cloud)
            implementation(projects.components.shop.cart)
            implementation(projects.components.core.ui)
            implementation(projects.components.core.koin)
            implementation(projects.components.core.intent.api)
            implementation(projects.components.core.intent.impl)
        }
    }
}