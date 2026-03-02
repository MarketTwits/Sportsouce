@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.kotlin.kmp.convention)
}
kotlin {

    android {
        namespace = "com.markettwits.intent.impl"
        androidResources {
            enable = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.core)
        }
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(projects.components.core.intent.api)
        }
    }
}
