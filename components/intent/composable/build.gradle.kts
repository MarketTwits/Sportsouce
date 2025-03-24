plugins {
    alias(libs.plugins.kotlin.kmp.compose.convention)
}

android {
    namespace = "com.markettwits.intent.composable"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.components.intent.api)
            implementation(projects.components.core.ui)
            implementation(projects.components.intent.impl)
        }
    }
}