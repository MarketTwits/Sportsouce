plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android { namespace = "com.markettwits.notification.api" }
    sourceSets.commonMain.dependencies {
        api(libs.bundles.decompose.compose)
        api(libs.kotlinx.coroutines.core)
        implementation(projects.components.core.ui)
        api(projects.components.auth.authFlow)
        api(projects.components.core.paging)
        api(libs.bundles.mviKotlin)
        implementation(libs.kotlinx.serialization.json)
    }
}
