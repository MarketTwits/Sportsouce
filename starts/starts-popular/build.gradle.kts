plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.popular"
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.time)
            implementation(projects.cloud)
            implementation(projects.core.ui)
            implementation(projects.core.koin)
            implementation(libs.koin.core)
            implementation(libs.bundles.decompose.compose)
            implementation(projects.starts.startsCommon)
            implementation(projects.start.start)
            implementation(libs.bundles.mviKotlin)
        }
    }
}
