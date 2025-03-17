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
            implementation(projects.cloud)
            implementation(projects.core.cache)
            implementation(projects.core.time)
            implementation(libs.koin.core)
        }
    }
}