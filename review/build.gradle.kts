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
        implementation(projects.settings.impl)
        implementation(projects.club.dashboard)
        implementation(projects.core.cache)
        implementation(projects.core.errors)
        implementation(projects.news.news)
        implementation(projects.news.newsCloud)
        implementation(projects.start.startSearch)
        implementation(projects.start.start)
        implementation(projects.starts.startsRandom)
        implementation(projects.starts.startsPopular)
        implementation(projects.core.time)
        implementation(projects.core.ui)
        implementation(projects.core.koin)
        implementation(projects.intent.api)
        implementation(projects.intent.impl)
        implementation(projects.starts.startsCommon)
        implementation(projects.selfupdater.components)
        implementation(projects.inappnotification.api)
        implementation(projects.shop.catalog)
        implementation(projects.core.log)
        implementation(libs.koin.core)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.bundles.mviKotlin)
    }
}