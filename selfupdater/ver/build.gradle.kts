plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.build.konfig.convension)
}

android.namespace = "com.markettwits.selfupdater.version"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
        }
    }
}