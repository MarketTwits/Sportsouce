plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.registrations"
}
kotlin {
    sourceSets.commonMain.dependencies {
        
        implementation(projects.auth.authService)
        implementation(projects.profile.cloud)
        implementation(projects.core.ui)
        implementation(projects.start.start)
        implementation(projects.core.koin)
        implementation(projects.core.time)
        implementation(projects.core.log)
        implementation(projects.intent.api)
        implementation(projects.intent.impl)
        implementation(libs.koin.core)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.bundles.mviKotlin)
    }
}