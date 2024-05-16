
plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose)
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
            implementation(projects.coreKoin)
            implementation(libs.bundles.mviKotlin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.junit)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1") {
                exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-debug")
            }
        }
    }
}
