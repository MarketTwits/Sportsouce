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
            implementation(projects.components.core.time)
            implementation(projects.components.core.ui)
            implementation(projects.components.core.koin)
            implementation(projects.components.core.errors)
            implementation(libs.koin.core)
            implementation(libs.bundles.decompose.compose)
            implementation(projects.components.starts.startsCommon)
            implementation(projects.components.start.start)
            implementation(libs.bundles.mviKotlin)
        }
    }
}
