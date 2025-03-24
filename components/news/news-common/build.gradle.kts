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
            implementation(projects.components.core.time)
            implementation(projects.components.core.koin)
            implementation(projects.components.core.log)
            implementation(projects.components.news.newsCloud)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.koin.core)
        }
    }
}