plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.build.konfig.convension)
}
android {
    namespace = "com.markettwits.news.cloud"
}

kotlin{
    sourceSets{
        commonMain.dependencies {
            implementation(projects.core.koin)
            implementation(projects.core.cloud)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
        }
    }
}
