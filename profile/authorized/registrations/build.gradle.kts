plugins {
    //alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.registrations"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.cloud)
        implementation(projects.auth.authService)
        implementation(projects.core.ui)
        implementation(projects.start.start)
        implementation(projects.coreKoin)
        implementation(projects.core.time)
        implementation(libs.koin.core)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.material3.html.text)
        implementation(libs.bundles.mviKotlin)
    }
}

//dependencies {
//    implementation(projects.cloud)
//    implementation(projects.auth.authService)
//    implementation(projects.core.ui)
//    implementation(projects.start.start)
//    implementation(projects.coreKoin)
//    implementation(projects.core.time)
//    implementation(libs.koin.core)
//    implementation(libs.bundles.decompose.compose)
//    implementation(libs.material3.html.text)
//    implementation(libs.bundles.mviKotlin)
//}