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
        api(projects.components.starts.startsCommon)
        api(projects.components.starts.startsFavorites)
        implementation(projects.components.core.time)
        implementation(projects.components.analytics.crashlytics)
        implementation(projects.components.start.startCloud)
        implementation(projects.components.core.intent.api)
        implementation(projects.components.core.intent.impl)
        implementation(projects.components.core.ui)
        implementation(projects.components.core.koin)
        implementation(projects.components.auth.authService)
        implementation(projects.components.start.startRegister)
        implementation(projects.components.start.startSupport)
        implementation(projects.components.core.cache)
        implementation(projects.components.core.decompose)
        implementation(projects.components.core.errors)
        implementation(projects.components.core.paging)
        implementation(projects.components.bottombar)
        implementation(projects.components.deeplink.api)
        implementation(libs.bundles.mviKotlin)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.koin.core)
    }
}