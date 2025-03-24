
plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}

android.namespace = "com.markettwits.inappnotification.impl"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.core.ui)
            implementation(projects.components.inappnotification.api)
        }
    }
}
