

plugins {
    alias(libs.plugins.kotlin.kmp.convention)
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.koin.core)
        implementation(projects.intent.api)
    }
    jvm()
}

android {
    namespace = "com.markettwits.intent.impl"
}