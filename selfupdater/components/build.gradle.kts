plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android.namespace = "com.markettwits.selfupdater.components"

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.inappnotification.api)
    implementation(projects.inappnotification.impl)
    implementation(projects.selfupdater.api)
    implementation(projects.selfupdater.impl)
    implementation(projects.selfupdater.thirdparty.api)
    implementation(projects.selfupdater.thirdparty.github)
    implementation(libs.bundles.composeUiBundle)
    implementation(libs.bundles.decompose.compose)
    implementation(projects.coreUi)
    implementation(libs.compose.markdown)
    implementation(libs.koin.core)
    implementation(projects.coreKoin)
    implementation(libs.bundles.mviKotlin)
}
