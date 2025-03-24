plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.unauthorized"
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.auth.authService)
            implementation(projects.components.core.ui)
            implementation(compose.components.resources)
            implementation(libs.koin.core)
            implementation(libs.compottie)
            implementation(libs.bundles.decompose.compose)
            implementation(libs.bundles.mviKotlin)
        }
    }
}