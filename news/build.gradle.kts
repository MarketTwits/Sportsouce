plugins {
    // alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.news"
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.cloud)
            implementation(projects.core.ui)
            implementation(projects.core.time)
            implementation(projects.core.koin)
            implementation(libs.koin.core)
            implementation(libs.bundles.decompose.compose)
            implementation(libs.bundles.mviKotlin)
        }
    }
}