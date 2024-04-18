plugins {
    //alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose)
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
            // pick image
            // implementation(libs.compose.activity)
            // implementation("io.github.mr0xf00:easycrop:0.1.1")

            implementation(libs.koin.core)
            implementation(libs.bundles.decompose.compose)
            implementation(libs.kotlinx.datetime)
            implementation(libs.bundles.mviKotlin)
        }
        androidMain.dependencies {
            implementation("io.github.onseok:peekaboo-ui:0.5.2")
            implementation("io.github.onseok:peekaboo-image-picker:0.5.2")
        }
    }
}
