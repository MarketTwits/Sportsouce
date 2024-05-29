plugins {
    // alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.start_support"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.intent.api)
        implementation(projects.intent.impl)
        implementation(projects.cloud)
        implementation(projects.core.ui)
        implementation(projects.coreKoin)
        implementation(libs.bundles.mviKotlin)
        implementation(libs.decompose)
        implementation(libs.koin.core)
    }
}