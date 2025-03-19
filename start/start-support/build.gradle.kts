plugins {
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
        implementation(projects.start.startCloud)
        implementation(projects.core.ui)
        implementation(projects.core.koin)
        implementation(compose.components.resources)
        implementation(libs.bundles.mviKotlin)
        implementation(libs.compottie)
        implementation(libs.decompose)
        implementation(libs.koin.core)
    }
}