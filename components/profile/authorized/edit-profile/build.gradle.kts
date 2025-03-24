plugins {
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
            implementation(projects.components.core.time)
            implementation(projects.components.profile.cloud)
            implementation(projects.components.auth.authService)
            implementation(projects.components.core.ui)
            implementation(projects.components.core.errors)
            implementation(projects.components.core.koin)
            implementation(projects.components.teamsCity)
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
