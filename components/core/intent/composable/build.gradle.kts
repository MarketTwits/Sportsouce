plugins {
    alias(libs.plugins.kotlin.kmp.compose.convention)
}

kotlin {
    android {
        namespace = "com.markettwits.intent.composable"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.components.core.intent.api)
            implementation(projects.components.core.ui)
            implementation(projects.components.core.intent.impl)
        }
    }
}
