plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
}
android {
    namespace = "com.markettwits.news.cloud"
}

kotlin{
    sourceSets{
        commonMain.dependencies {
            implementation(projects.components.core.koin)
            implementation(projects.components.core.cloud)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
        }
    }
}
