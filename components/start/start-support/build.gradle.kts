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
        implementation(projects.components.intent.api)
        implementation(projects.components.intent.impl)
        implementation(projects.components.start.startCloud)
        implementation(projects.components.core.ui)
        implementation(projects.components.core.koin)
        implementation(compose.components.resources)
        implementation(libs.bundles.mviKotlin)
        implementation(libs.compottie)
        implementation(libs.decompose)
        implementation(libs.koin.core)
    }
}