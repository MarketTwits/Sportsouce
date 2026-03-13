@file:Suppress("UnstableApiUsage")
@file:OptIn(org.jetbrains.kotlin.gradle.ExternalKotlinTargetApi::class)

import com.markettwits.sportsouce.extensions.libs

plugins {
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose")
    id("kotlin.multiplatform.convention")
}

kotlin {
    android {
        androidResources {
            enable = true
        }
    }
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.ui.tooling.preview)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}
