plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.start_filter"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.core.ui)
        implementation(projects.core.koin)
        implementation(projects.start.start)
        implementation(projects.starts.startsCommon)
        implementation(projects.core.cache)
        implementation(libs.koin.core)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.bundles.mviKotlin)

    }
}