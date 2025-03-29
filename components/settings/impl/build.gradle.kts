plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}

android.namespace = "com.markettwits.settings.impl"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.core.intent.api)
            implementation(projects.components.core.intent.impl)
            implementation(projects.components.selfupdater.components)
            implementation(projects.components.selfupdater.api)
            implementation(projects.components.selfupdater.ver)
            implementation(projects.components.settings.api)
            implementation(libs.kotlinx.serialization.json)
            implementation(projects.components.core.cache)
            implementation(projects.components.core.ui)
            implementation(libs.koin.core)
            implementation(projects.components.core.koin)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.bundles.decompose.compose)
            implementation(libs.bundles.mviKotlin)
        }
    }
}
