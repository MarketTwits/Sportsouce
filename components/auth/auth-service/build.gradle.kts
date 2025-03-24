plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}
android{
    namespace = "com.markettwits.auth.auth_service"
}
kotlin{
    sourceSets{
        commonMain.dependencies {
            api(projects.components.auth.authCloud)
            implementation(projects.components.core.cache)
            implementation(projects.components.core.time)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
        }
    }
}