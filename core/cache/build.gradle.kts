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
            api(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.core)
            implementation(projects.core.koin)
            implementation(libs.koin.core)
        }
        jvmMain.dependencies {
            api(libs.kstore.file)
        }
        androidMain.dependencies {
            api(libs.kstore.file)
        }
        jsMain.dependencies {
            api(libs.kstore.storage)
        }
    }
}