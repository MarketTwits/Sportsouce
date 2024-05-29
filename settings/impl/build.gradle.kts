plugins {
    //  alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}

android.namespace = "com.markettwits.settings.impl"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.intent.api)
            implementation(projects.intent.impl)
            implementation(projects.selfupdater.components)
            implementation(projects.selfupdater.api)
            implementation(projects.settings.api)
            implementation(libs.kotlinx.serialization.json)
            implementation(projects.cache)
            implementation(projects.core.ui)
            implementation(libs.koin.core)
            implementation(projects.coreKoin)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.bundles.decompose.compose)
            implementation(libs.bundles.mviKotlin)
        }
    }
}

//dependencies {
//    implementation(projects.intent.api)
//    implementation(projects.intent.impl)
//    implementation(projects.selfupdater.components)
//    implementation(projects.settings.api)
//    implementation(libs.kotlinx.serialization.json)
//    implementation(projects.cache)
//    implementation(projects.core.ui)
//    implementation(libs.koin.core)
//    implementation(projects.coreKoin)
//    implementation(libs.kotlinx.coroutines.core)
//    implementation(libs.bundles.decompose.compose)
//    implementation(libs.bundles.mviKotlin)
//    implementation(projects.selfupdater.api)
//    implementation(libs.bundles.composeUiBundle)
//}
