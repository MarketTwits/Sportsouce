plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)

}

android {
    namespace = "com.markettwits.profile"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.analytics.crashlytics)
        implementation(projects.core.time)
        implementation(projects.settings.impl)
        implementation(projects.start.start)
        implementation(projects.cloud)
        implementation(projects.auth.authService)
        implementation(projects.core.ui)
        implementation(projects.coreKoin)
        implementation(projects.profile.authorized.editProfile)
        implementation(projects.profile.authorized.registrations)
        implementation(projects.profile.authorized.members)
        implementation(projects.cache)
        implementation(projects.intent.api)
        implementation(projects.intent.impl)
        implementation(projects.starts.startsCommon)
        implementation(libs.koin.core)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.bundles.mviKotlin)
    }
}