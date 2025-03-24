plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.auth_flow"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.components.analytics.crashlytics)
        implementation(projects.components.auth.authService)
        implementation(projects.components.core.ui)
        implementation(projects.components.core.koin)
        implementation(projects.components.core.errors)
        implementation(libs.koin.core)
        implementation(compose.components.resources)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.bundles.mviKotlin)
        implementation(projects.components.core.time)
    }
}