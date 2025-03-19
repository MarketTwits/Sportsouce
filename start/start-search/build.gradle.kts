plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.start_search"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.core.time)
        implementation(projects.core.cache)
        implementation(projects.core.errors)
        implementation(projects.core.ui)
        implementation(projects.core.koin)
        implementation(projects.start.startCloud)
        implementation(projects.teamsCity)
        implementation(projects.start.start)
        implementation(projects.starts.startsCommon)
        implementation(libs.bundles.mviKotlin)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.koin.core)
    }
}