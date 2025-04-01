plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.sportsauce.root"
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.bottombar)
            implementation(projects.components.starts.starts)
            implementation(projects.components.profile.root)
            implementation(projects.components.review)
            implementation(projects.components.core.ui)
            implementation(projects.components.core.koin)
            implementation(libs.bundles.decompose.compose)
            implementation(libs.koin.core)
        }
    }
}