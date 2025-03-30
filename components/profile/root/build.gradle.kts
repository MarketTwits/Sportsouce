plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.profile.unauthorized)
            implementation(projects.components.profile.authorized)
            implementation(projects.components.auth.authFlow)
            implementation(projects.components.core.koin)
            implementation(libs.koin.core)
            implementation(projects.components.core.ui)
            implementation(libs.bundles.decompose.compose)
            implementation(libs.bundles.mviKotlin)
        }
    }
}
android {
    namespace = "com.markettwits.profile.root"
}
