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
        implementation(projects.components.analytics.crashlytics)

        implementation(projects.components.core.cache)
        implementation(projects.components.core.errors)
        implementation(projects.components.core.koin)
        implementation(projects.components.core.time)
        implementation(projects.components.core.ui)

        implementation(projects.components.core.intent.api)
        implementation(projects.components.core.intent.composable)
        implementation(projects.components.core.intent.impl)

        implementation(projects.components.auth.authService)

        implementation(projects.components.profile.editProfile)
        implementation(projects.components.profile.members)
        implementation(projects.components.profile.registrations)
        implementation(projects.components.profile.cloud)
        
        implementation(projects.components.club.dashboard)
        implementation(projects.components.settings.impl)
        implementation(projects.components.shop.orders)
        implementation(projects.components.start.start)
        implementation(projects.components.starts.startsCommon)

        implementation(libs.bundles.decompose.compose)
        implementation(libs.bundles.mviKotlin)
        implementation(libs.koin.core)
        implementation(libs.kotlinx.serialization.json)
    }
}