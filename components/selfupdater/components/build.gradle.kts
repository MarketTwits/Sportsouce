
plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android.namespace = "com.markettwits.selfupdater.components"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(projects.components.inappnotification.api)
            implementation(projects.components.inappnotification.impl)
            implementation(projects.components.selfupdater.api)
            implementation(projects.components.selfupdater.impl)
            implementation(projects.components.selfupdater.thirdparty.api)
            implementation(projects.components.selfupdater.thirdparty.github)
            implementation(libs.bundles.decompose.compose)
            implementation(projects.components.core.ui)
            implementation(libs.multiplatform.markdown.renderer.m3)
            implementation(libs.koin.core)
            implementation(projects.components.core.koin)
            implementation(libs.bundles.mviKotlin)
        }
        commonTest.dependencies {
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.kotlin.test)
        }
    }
}
