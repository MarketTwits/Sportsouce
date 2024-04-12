plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android.namespace = "com.markettwits.settings.impl"

dependencies {
    implementation(projects.intent.api)
    implementation(projects.intent.impl)
    implementation(projects.selfupdater.components)
    implementation(projects.settings.api)
    implementation(libs.kotlinx.serialization.json)
    implementation(projects.cache)
    implementation(projects.coreUi)
    implementation(libs.koin.core)
    implementation(projects.coreKoin)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.bundles.mviKotlin)
    implementation(projects.selfupdater.api)
    implementation(libs.bundles.composeUiBundle)

}
