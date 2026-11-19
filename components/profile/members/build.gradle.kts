plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {

    android {
        namespace = "com.markettwits.members"
    }

    sourceSets.commonTest.dependencies {
        implementation(libs.kotlin.test)
        implementation(libs.kotlinx.coroutines.test)
    }

    sourceSets.commonMain.dependencies {
        implementation(projects.components.core.time)
        implementation(libs.kotlinx.datetime)
        implementation(projects.components.core.cache)
        implementation(projects.components.profile.cloud)
        implementation(projects.components.core.ui)
        implementation(projects.components.core.errors)
        implementation(projects.components.auth.authService)
        implementation(projects.components.core.koin)
        implementation(projects.components.teamsCity)
        implementation(projects.components.analytics.crashlytics)
        implementation(projects.components.bottombar)
        implementation(libs.koin.core)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.bundles.mviKotlin)
    }
}
