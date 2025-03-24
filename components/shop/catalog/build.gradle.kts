plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}

android.namespace = "com.markettwits.shop.catalog"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.decompose.compose)
            implementation(libs.koin.core)
            implementation(libs.bundles.mviKotlin)
            implementation(projects.components.bottombar)
            implementation(projects.components.auth.authFlow)
            implementation(projects.components.core.errors)
            implementation(projects.components.shop.filter)
            implementation(projects.components.shop.cloud)
            implementation(projects.components.shop.item)
            implementation(projects.components.shop.search)
            implementation(projects.components.shop.cart)
            implementation(projects.components.shop.domain)
            implementation(projects.components.shop.order)
            implementation(projects.components.shop.orders)
            implementation(projects.components.core.ui)
            implementation(projects.components.core.koin)
            implementation(projects.components.core.paging)
        }
    }
}