plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.schedule"
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.cache)
            implementation(projects.cloud)
            implementation(projects.core.ui)
            implementation(projects.coreKoin)
            implementation(projects.start.start)
            implementation(projects.starts.startsCommon)
            implementation(libs.koin.core)
            implementation(libs.bundles.decompose.compose)
            implementation(libs.bundles.mviKotlin)
        }
    }
}