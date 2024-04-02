plugins {
    alias(libs.plugins.android.library.convention)
    alias(libs.plugins.kotlin.serialization)
}

android.namespace = "com.markettwits.sportsauce.settings.api"

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.core)
}

