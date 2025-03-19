plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.starts"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.settings.impl)
        implementation(projects.core.time)
        implementation(projects.core.cache)
        implementation(projects.core.errors)
        implementation(projects.core.koin)
        implementation(projects.start.start)
        implementation(projects.start.startSearch)
        implementation(projects.core.ui)
        implementation(projects.starts.startsCommon)
        implementation(libs.koin.core)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.kotlinx.serialization.json)
    }
}