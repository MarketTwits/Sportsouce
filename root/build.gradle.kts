
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
            implementation(projects.bottombar)
            implementation(projects.starts.starts)
            implementation(projects.profile.root)
            implementation(projects.review.review)
            implementation(projects.core.ui)
            implementation(projects.core.koin)
            implementation(libs.bundles.decompose.compose)
            implementation(libs.koin.core)
        }
    }
}