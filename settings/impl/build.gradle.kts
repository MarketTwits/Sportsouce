plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}

android.namespace = "com.markettwits.settings.impl"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.intent.api)
            implementation(projects.intent.impl)
            implementation(projects.selfupdater.components)
            implementation(projects.selfupdater.api)
            implementation(projects.selfupdater.ver)
            implementation(projects.settings.api)
            implementation(libs.kotlinx.serialization.json)
            implementation(projects.core.cache)
            implementation(projects.core.ui)
            implementation(libs.koin.core)
            implementation(projects.core.koin)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.bundles.decompose.compose)
            implementation(libs.bundles.mviKotlin)
        }
    }
}
