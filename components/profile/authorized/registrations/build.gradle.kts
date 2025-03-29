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
        
        implementation(projects.components.auth.authService)
        implementation(projects.components.profile.cloud)
        implementation(projects.components.core.ui)
        implementation(projects.components.start.start)
        implementation(projects.components.core.koin)
        implementation(projects.components.core.time)
        implementation(projects.components.core.log)
        implementation(projects.components.core.intent.api)
        implementation(projects.components.core.intent.impl)
        implementation(libs.koin.core)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.bundles.mviKotlin)
    }
}