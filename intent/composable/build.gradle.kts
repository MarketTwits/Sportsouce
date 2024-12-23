plugins {
    alias(libs.plugins.kotlin.kmp.compose.convention)
}

android {
    namespace = "com.markettwits.intent.composable"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.intent.api)
            implementation(projects.core.ui)
            implementation(projects.intent.impl)
        }
    }
}