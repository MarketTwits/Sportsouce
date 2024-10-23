plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    id("build-config-convention")
}

android.namespace = "com.markettwits.selfupdater.version"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
        }
    }
}