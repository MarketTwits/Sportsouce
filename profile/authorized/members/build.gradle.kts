plugins {
    //alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.members"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.core.time)
        implementation(projects.cache)
        implementation(projects.cloud)
        implementation(projects.core.ui)
        implementation(projects.auth.authService)
        implementation(projects.coreKoin)
        implementation(projects.teamsCity)
        implementation(libs.koin.core)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.bundles.mviKotlin)
    }
}

//dependencies {
//    implementation(projects.core.time)
//    implementation(projects.cache)
//    implementation(projects.cloud)
//    implementation(projects.core.ui)
//    implementation(projects.auth.authService)
//    implementation(projects.coreKoin)
//    implementation(projects.teamsCity)
//    implementation(libs.koin.core)
//    implementation(libs.bundles.decompose.compose)
//    implementation(libs.bundles.mviKotlin)
//}