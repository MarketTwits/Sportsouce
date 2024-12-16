plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}

android.namespace = "com.markettwits.core.paging"

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.paging.common)
            api(libs.paging.compose.common)
        }
    }
}