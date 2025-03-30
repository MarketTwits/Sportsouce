plugins {
    alias(libs.plugins.kotlin.kmp.convention)
}

android.namespace = "com.markettwits.selfupdater.version"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(projects.components.core.buildConfig)
        }
    }
}