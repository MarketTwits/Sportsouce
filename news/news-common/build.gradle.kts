plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.news.common"
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.time)
            implementation(projects.core.koin)
            implementation(projects.core.log)
            implementation(projects.news.newsCloud)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.koin.core)
        }
    }
}