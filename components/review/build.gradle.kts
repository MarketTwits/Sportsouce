plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.review"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.components.settings.impl)
        implementation(projects.components.club.dashboard)
        implementation(projects.components.core.cache)
        implementation(projects.components.core.errors)
        implementation(projects.components.news.news)
        implementation(projects.components.start.startSearch)
        implementation(projects.components.start.start)
        implementation(projects.components.starts.startsRandom)
        implementation(projects.components.starts.startsPopular)
        implementation(projects.components.core.time)
        implementation(projects.components.core.ui)
        implementation(projects.components.core.koin)
        implementation(projects.components.core.intent.api)
        implementation(projects.components.core.intent.impl)
        implementation(projects.components.starts.startsCommon)
        implementation(projects.components.selfupdater.components)
        implementation(projects.components.inappnotification.api)
        implementation(projects.components.shop.catalog)
        implementation(projects.components.core.log)
        implementation(libs.koin.core)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.bundles.mviKotlin)
    }
}