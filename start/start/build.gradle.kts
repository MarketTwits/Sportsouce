plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}

android {
    namespace = "com.markettwits.start"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.core.time)
        implementation(projects.analytics.crashlytics)
        implementation(projects.cloud)
        implementation(projects.start.startCloud)
        implementation(projects.core.ui)
        implementation(projects.core.koin)
        implementation(projects.auth.authService)
        implementation(projects.start.startRegister)
        implementation(projects.start.startSupport)
        implementation(projects.core.cache)
        implementation(projects.bottombar)
        implementation(libs.bundles.mviKotlin)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.koin.core)
        implementation(projects.intent.api)
        implementation(projects.intent.impl)
    }
}