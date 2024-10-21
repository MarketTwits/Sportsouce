import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
}

android.namespace = "com.markettwits.shop.catalog"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.decompose.compose)
            implementation(libs.koin.core)
            implementation(libs.bundles.mviKotlin)
            implementation(projects.shop.filter)
            implementation(projects.shop.cloud)
            implementation(projects.shop.item)
            implementation(projects.shop.search)
            implementation(projects.shop.cart)
            implementation(projects.core.ui)
            implementation(projects.coreKoin)
            implementation("app.cash.paging:paging-common:3.3.0-alpha02-0.4.0")
            implementation("app.cash.paging:paging-compose-common:3.3.0-alpha02-0.5.1")
        }
    }
}
