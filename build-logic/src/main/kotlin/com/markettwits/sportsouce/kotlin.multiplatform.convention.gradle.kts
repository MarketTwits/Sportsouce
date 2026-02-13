@file:OptIn(org.jetbrains.kotlin.gradle.ExternalKotlinTargetApi::class)

plugins {
    id("com.android.kotlin.multiplatform.library")
    id("org.jetbrains.kotlin.multiplatform")
}

commonKmpJava()

kotlin {
    android {
        commonAndroid()
    }
    jvm()
    js {
        browser()
        nodejs()
        binaries.executable()
    }
}
