plugins {
    alias(libs.plugins.android.library.convention)
    alias(libs.plugins.kotlin.serialization)
}

android.namespace = "com.markettwits.selfupdater.impl"

dependencies {
    implementation(projects.selfupdater.api)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.core)
}
