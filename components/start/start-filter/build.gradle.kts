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
        implementation(projects.components.core.ui)
        implementation(projects.components.core.koin)
        implementation(projects.components.start.start)
        implementation(projects.components.starts.startsCommon)
        implementation(projects.components.core.cache)
        implementation(libs.koin.core)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.bundles.mviKotlin)

    }
}