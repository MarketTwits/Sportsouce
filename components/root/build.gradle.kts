plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {

    android {
        namespace = "com.markettwits.sportsauce.root"
    }

    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        commonMain.dependencies {
            implementation(projects.components.bottombar)
            implementation(projects.components.starts.starts)
            implementation(projects.components.profile.root)
            implementation(projects.components.review)
            implementation(projects.components.selfupdater.components)
            implementation(projects.components.inappnotification.api)
            implementation(projects.components.core.ui)
            implementation(projects.components.core.koin)
            implementation(projects.components.deeplink.api)
            implementation(libs.bundles.decompose.compose)
            implementation(libs.koin.core)
        }
    }
}
