plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}

kotlin {

    android {
        namespace = "com.markettwits.theme"
    }

    sourceSets.commonMain.dependencies {
        implementation(libs.decompose)
        implementation(libs.koin.core)
        implementation(projects.components.core.koin)
        implementation(projects.components.core.ui)
        implementation(projects.components.settings.api)
    }
}
