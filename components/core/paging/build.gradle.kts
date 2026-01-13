plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}


kotlin {

    android {
        namespace = "com.markettwits.core.paging"
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.paging.common)
            api(libs.paging.compose.common)
        }
    }

    js()
}
