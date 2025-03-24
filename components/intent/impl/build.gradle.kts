

plugins {
    alias(libs.plugins.kotlin.kmp.convention)
}
android {
    namespace = "com.markettwits.intent.impl"
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(projects.components.intent.api)
        }
    }
}

