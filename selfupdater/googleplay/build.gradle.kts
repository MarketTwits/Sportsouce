plugins {
    alias(libs.plugins.kotlin.kmp.convention)
}

android.namespace = "com.markettwits.selfupdater.googleplay"

dependencies {


    implementation(projects.selfupdater.api)
    implementation(projects.inappnotification.api)
    implementation(projects.core.activityholder)
    implementation("com.google.android.play:app-update:2.1.0")

    // For Kotlin users also add the Kotlin extensions library for Play In-App Update:
    implementation("com.google.android.play:app-update-ktx:2.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")
//    implementation(projects.components.selfupdater.api)
//    implementation(projects.components.inappnotification.api)
//
//    implementation(projects.components.core.log)
//    implementation(projects.components.core.activityholder)
//
//    // In-app update
//    implementation(libs.app.update)
//    implementation(libs.app.update.ktx)
//    implementation(libs.app.update.ktx)
//
//    implementation(libs.kotlin.coroutines.play.services)
//
//    // Dagger deps
//    implementation(projects.components.core.di)
}
