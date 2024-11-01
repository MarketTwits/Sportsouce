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
            implementation(projects.shop.domain)
            implementation(projects.cache)
            implementation(projects.core.ui)
            implementation(projects.coreKoin)
            implementation(projects.shop.cloud)
        }
    }
}