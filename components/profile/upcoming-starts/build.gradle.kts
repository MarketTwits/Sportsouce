plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android { namespace = "com.markettwits.sportsouce.profile.upcoming" }

    sourceSets.commonMain.dependencies {
        implementation(projects.components.profile.registrations)
        implementation(projects.components.auth.authFlow)
        implementation(projects.components.auth.authCloud)
        implementation(projects.components.core.errors)
        implementation(projects.components.core.koin)
        implementation(projects.components.core.log)
        implementation(projects.components.core.time)
        implementation(projects.components.core.ui)
        implementation(projects.components.notification.api)
        implementation(projects.components.notification.impl)
        implementation(projects.components.selfupdater.components)
        implementation(projects.components.settings.impl)
        implementation(projects.components.start.start)
        implementation(projects.components.start.startSearch)
        implementation(projects.components.starts.startsCommon)
        implementation(projects.components.starts.startsCloud)
        implementation(projects.components.starts.startsAlbums)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.bundles.mviKotlin)
        implementation(libs.coil.compose)
        implementation(libs.koin.core)
        implementation(libs.ktor.core)
        implementation(libs.kotlinx.datetime)
    }

    sourceSets.commonTest.dependencies {
        implementation(libs.kotlin.test)
        implementation(libs.kotlinx.coroutines.test)
    }
}
