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
        implementation(projects.components.core.ui)
        implementation(projects.components.core.errors)
        implementation(projects.components.core.koin)
        implementation(projects.components.auth.authService)
        implementation(projects.components.auth.authFlow)
        implementation(projects.components.teamsCity)
        implementation(projects.components.start.startCloud)
        implementation(projects.components.core.time)
        implementation(projects.components.profile.authorized.members)
        implementation(projects.components.intent.api)
        implementation(projects.components.intent.impl)
        implementation(libs.bundles.mviKotlin)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.koin.core)
        implementation(projects.components.analytics.crashlytics)
    }
}