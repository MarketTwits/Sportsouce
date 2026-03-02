plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {

    android {
        namespace = "com.markettwits.start_support"
    }

    sourceSets.commonMain.dependencies {
        implementation(projects.components.core.intent.api)
        implementation(projects.components.core.intent.impl)
        implementation(projects.components.start.startCloud)
        implementation(projects.components.core.ui)
        implementation(projects.components.core.koin)
        implementation(libs.compose.components.resources)
        implementation(libs.bundles.mviKotlin)
        implementation(libs.compottie)
        implementation(libs.decompose)
        implementation(libs.koin.core)
    }
}
