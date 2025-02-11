
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
            implementation(projects.inappnotification.api)
            implementation(projects.inappnotification.impl)
            implementation(projects.selfupdater.api)
            implementation(projects.selfupdater.impl)
            implementation(projects.selfupdater.thirdparty.api)
            implementation(projects.selfupdater.thirdparty.github)
            implementation(libs.bundles.decompose.compose)
            implementation(projects.core.ui)
            implementation(libs.multiplatform.markdown.renderer.m3)
            implementation(libs.koin.core)
            implementation(projects.core.koin)
            implementation(libs.bundles.mviKotlin)
        }
        commonTest.dependencies {
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.kotlin.test)
        }
    }
}
