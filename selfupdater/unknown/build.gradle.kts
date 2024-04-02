plugins {
    alias(libs.plugins.android.library.convention)
    alias(libs.plugins.kotlin.serialization)
}

android.namespace = "com.markettwits.selfupdater.unknown"

dependencies {
    implementation(projects.selfupdater.api)
//    implementation(projects.components.selfupdater.api)
//
//    implementation(projects.components.core.di)
}
