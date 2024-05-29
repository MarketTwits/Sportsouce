plugins {
    //alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.edit_profile"
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.time)
            implementation(projects.cloud)
            implementation(projects.auth.authService)
            implementation(projects.core.ui)
            implementation(projects.coreKoin)
            implementation(projects.teamsCity)
            implementation(libs.koin.core)
            implementation(libs.bundles.decompose.compose)
            implementation(libs.bundles.mviKotlin)
        }
        androidMain.dependencies {
            implementation(libs.compose.activity)
            implementation("io.github.mr0xf00:easycrop:0.1.1")
        }
    }
}
