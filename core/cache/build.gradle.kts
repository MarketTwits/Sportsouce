plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}
android{
    namespace = "com.markettwits.cache"
}
kotlin{
    sourceSets{
        commonMain.dependencies {
            api(libs.kstore)
            api(libs.kstore.file)
            implementation(projects.core.koin)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.koin.core)
        }
    }
}