plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {

    android {
        namespace = "com.markettwits.settings.api"
    }

    sourceSets.commonMain.dependencies {
        implementation(projects.components.core.cache)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.koin.core)
    }
}
