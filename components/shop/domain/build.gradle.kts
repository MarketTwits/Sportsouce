plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
}

android.namespace = "com.markettwits.shop.domain"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.shop.cloud)
        }
    }
}
