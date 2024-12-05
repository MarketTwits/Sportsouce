plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}

android{
    namespace = "com.markettwits.teams_cities"
}
kotlin{
    sourceSets.commonMain.dependencies {
        implementation(projects.core.koin)
        implementation(projects.cloud)
        implementation(projects.core.cache)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.kotlinx.serialization.core)
        implementation(libs.koin.core)
    }
}