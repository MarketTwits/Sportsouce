plugins {
    //  alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.sportsauce.bottombar"
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.ui)
            implementation(libs.koin.core)
            implementation(projects.coreKoin)
            implementation(libs.bundles.decompose.compose)
        }
    }
}