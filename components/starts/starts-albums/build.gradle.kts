plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android { namespace = "com.markettwits.starts.albums" }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.starts.startsCloud)
            implementation(projects.components.start.start)
            implementation(projects.components.core.ui)
            implementation(projects.components.core.time)
            implementation(projects.components.core.paging)
            implementation(projects.components.core.koin)
            implementation(projects.components.core.decompose)
            implementation(projects.components.bottombar)
            implementation(libs.koin.core)
            implementation(libs.bundles.mviKotlin)
            implementation(libs.kotlinx.serialization.json)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
