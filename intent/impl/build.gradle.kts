plugins {
    alias(libs.plugins.android.library.convention)
}

android {
    namespace = "com.markettwits.intent.impl"
}

dependencies {
    implementation(libs.koin.core)
    implementation(projects.intent.api)
}