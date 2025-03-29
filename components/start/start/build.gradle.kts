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
        implementation(projects.components.core.time)
        implementation(projects.components.analytics.crashlytics)
        implementation(projects.components.start.startCloud)
        implementation(projects.components.starts.startsCommon)
        implementation(projects.components.core.ui)
        implementation(projects.components.core.koin)
        implementation(projects.components.auth.authService)
        implementation(projects.components.start.startRegister)
        implementation(projects.components.start.startSupport)
        implementation(projects.components.core.cache)
        implementation(projects.components.core.decompose)
        implementation(projects.components.core.errors)
        implementation(projects.components.bottombar)
        implementation(libs.bundles.mviKotlin)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.koin.core)
        implementation(projects.components.core.intent.api)
        implementation(projects.components.core.intent.impl)
    }
}