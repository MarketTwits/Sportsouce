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
            implementation(projects.auth.authFlow)
            implementation(projects.core.errors)
            implementation(projects.shop.filter)
            implementation(projects.shop.cloud)
            implementation(projects.shop.item)
            implementation(projects.shop.search)
            implementation(projects.shop.cart)
            implementation(projects.shop.domain)
            implementation(projects.shop.order)
            implementation(projects.shop.orders)
            implementation(projects.core.ui)
            implementation(projects.core.koin)
            implementation(projects.core.paging)
        }
    }
}