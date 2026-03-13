plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {

    android {
        namespace = "com.markettwits.edit_profile"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.core.time)
            implementation(projects.components.profile.cloud)
            implementation(projects.components.auth.authService)
            implementation(projects.components.core.ui)
            implementation(projects.components.core.errors)
            implementation(projects.components.core.koin)
            implementation(projects.components.teamsCity)
            implementation(projects.components.starts.startsRecent)
            implementation(libs.koin.core)
            implementation(libs.krop.ui)
            implementation(libs.bundles.decompose.compose)
            implementation(libs.bundles.mviKotlin)
        }
        androidMain.dependencies {
            implementation(libs.compose.activity)
        }
    }
}
