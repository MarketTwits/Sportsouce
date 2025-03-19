plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.start_register"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.core.ui)
        implementation(projects.core.errors)
        implementation(projects.core.koin)
        implementation(projects.auth.authService)
        implementation(projects.auth.authFlow)
        implementation(projects.teamsCity)
        implementation(projects.start.startCloud)
        implementation(projects.core.time)
        implementation(projects.profile.authorized.members)
        implementation(projects.intent.api)
        implementation(projects.intent.impl)
        implementation(libs.bundles.mviKotlin)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.koin.core)
        implementation(projects.analytics.crashlytics)
    }
}