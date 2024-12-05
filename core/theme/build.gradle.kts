plugins {
    //alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}

android {
    namespace = "com.markettwits.theme"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.decompose)
        implementation(libs.koin.core)
        implementation(projects.core.koin)
        implementation(projects.core.ui)
        implementation(projects.settings.api)
    }
}