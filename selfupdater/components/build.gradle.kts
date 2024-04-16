
plugins {
    // alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose)
    alias(libs.plugins.kotlin.serialization)
}

android.namespace = "com.markettwits.selfupdater.components"
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(projects.inappnotification.api)
            implementation(projects.inappnotification.impl)
            implementation(projects.selfupdater.api)
            implementation(projects.selfupdater.impl)
            implementation(projects.selfupdater.thirdparty.api)
            implementation(projects.selfupdater.thirdparty.github)
            implementation(libs.bundles.decompose.compose)
            implementation(projects.core.ui)
            implementation(libs.compose.markdown)
            implementation(libs.koin.core)
            implementation(projects.coreKoin)
            implementation(libs.bundles.mviKotlin)
        }
    }
}
//dependencies {
//    implementation(libs.kotlinx.coroutines.core)
//    implementation(projects.inappnotification.api)
//    implementation(projects.inappnotification.impl)
//    implementation(projects.selfupdater.api)
//    implementation(projects.selfupdater.impl)
//    implementation(projects.selfupdater.thirdparty.api)
//    implementation(projects.selfupdater.thirdparty.github)
//    implementation(libs.bundles.composeUiBundle)
//    implementation(libs.bundles.decompose.compose)
//    implementation(projects.core.ui)
//    implementation(libs.compose.markdown)
//    implementation(libs.koin.core)
//    implementation(projects.coreKoin)
//    implementation(libs.bundles.mviKotlin)
//}
