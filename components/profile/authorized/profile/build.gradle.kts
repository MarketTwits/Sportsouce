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
        implementation(projects.components.core.time)
        implementation(projects.components.settings.impl)
        implementation(projects.components.start.start)
        implementation(projects.components.profile.cloud)
        implementation(projects.components.auth.authService)
        implementation(projects.components.core.ui)
        implementation(projects.components.core.koin)
        implementation(projects.components.core.errors)
        implementation(projects.components.profile.authorized.editProfile)
        implementation(projects.components.profile.authorized.registrations)
        implementation(projects.components.profile.authorized.members)
        implementation(projects.components.shop.orders)
        implementation(projects.components.core.cache)
        implementation(projects.components.core.intent.api)
        implementation(projects.components.core.intent.impl)
        implementation(projects.components.starts.startsCommon)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.koin.core)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.bundles.mviKotlin)
    }
}