plugins {
    // alias(libs.plugins.android.library.convention)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}

android.namespace = "com.markettwits.selfupdater.thirdparty.api"

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.selfupdater.api)
        implementation(projects.inappnotification.api)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.koin.core)
    }
}

//dependencies {
//    implementation(projects.selfupdater.api)
//    implementation(projects.inappnotification.api)
//    implementation(libs.kotlinx.coroutines.core)
//    implementation(libs.koin.core)
//    implementation(libs.koin.android)
//}
