plugins {
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
            api(projects.news.newsCommon)
            implementation(projects.core.ui)
            implementation(projects.core.time)
            implementation(projects.core.koin)
            implementation(projects.core.decompose)
            implementation(libs.koin.core)
            implementation(libs.bundles.mviKotlin)
        }
    }
}