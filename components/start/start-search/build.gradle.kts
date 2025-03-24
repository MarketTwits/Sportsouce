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
        implementation(projects.components.core.time)
        implementation(projects.components.core.cache)
        implementation(projects.components.core.errors)
        implementation(projects.components.core.ui)
        implementation(projects.components.core.koin)
        implementation(projects.components.start.startCloud)
        implementation(projects.components.teamsCity)
        implementation(projects.components.start.start)
        implementation(projects.components.starts.startsCommon)
        implementation(libs.bundles.mviKotlin)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.koin.core)
    }
}