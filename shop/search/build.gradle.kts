plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}

android.namespace = "com.markettwits.shop.search"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.decompose.compose)
            implementation(libs.koin.core)
            implementation(libs.bundles.mviKotlin)
            implementation(projects.core.ui)
            implementation(projects.core.koin)
        }
    }
}