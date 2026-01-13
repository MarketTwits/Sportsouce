plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {

    android {
        namespace = "com.markettwits.sportsauce.bottombar"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.core.ui)
            implementation(libs.koin.core)
            implementation(projects.components.core.koin)
            implementation(projects.components.settings.api)
            implementation(libs.bundles.decompose.compose)
            implementation(libs.composeMaterial3AdaptiveNavigationSuite)
        }
    }
}
