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
            api(projects.components.news.newsCommon)
            implementation(projects.components.core.ui)
            implementation(projects.components.core.time)
            implementation(projects.components.core.koin)
            implementation(projects.components.core.decompose)
            implementation(libs.koin.core)
            implementation(libs.bundles.mviKotlin)
        }
    }
}