plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}
kotlin {

    android {
        namespace = "com.markettwits.auth.auth_service"
    }

    sourceSets{
        commonMain.dependencies {
            api(projects.components.auth.authCloud)
            implementation(projects.components.core.cache)
            implementation(projects.components.core.time)
            implementation(projects.components.core.log)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
        }
    }
}
