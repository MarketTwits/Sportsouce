plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.review"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.club.dashboard)
        implementation(projects.cache)
        implementation(projects.cloud)
        implementation(projects.news)
        implementation(projects.start.startSearch)
        implementation(projects.start.start)
        implementation(projects.review.random)
        implementation(projects.review.schedule)
        implementation(projects.review.popular)
        implementation(projects.core.time)
        implementation(projects.core.ui)
        implementation(projects.coreKoin)
        implementation(projects.intent.api)
        implementation(projects.intent.impl)
        implementation(projects.starts.startsCommon)
        implementation(projects.selfupdater.components)
        implementation(projects.inappnotification.api)
        implementation(libs.koin.core)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.bundles.mviKotlin)
    }
}