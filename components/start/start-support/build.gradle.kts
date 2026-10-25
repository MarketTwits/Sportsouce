plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {

    sourceSets.commonTest.dependencies {
        implementation(libs.kotlin.test)
        implementation(libs.kotlinx.coroutines.test)
        implementation(projects.components.core.cloud)
        implementation("io.ktor:ktor-client-mock:${libs.versions.ktor.get()}")
    }

    android {
        namespace = "com.markettwits.start_support"
    }

    sourceSets.commonMain.dependencies {
        implementation(projects.components.core.intent.api)
        implementation(projects.components.core.intent.impl)
        implementation(projects.components.start.startCloud)
        implementation(projects.components.core.ui)
        implementation(projects.components.auth.authService)
        implementation(projects.components.profile.cloud)
        implementation(projects.components.core.decompose)
        implementation(libs.ktor.core)
        implementation(projects.components.core.koin)
        implementation(libs.compose.components.resources)
        implementation(libs.bundles.mviKotlin)
        implementation(libs.compottie)
        implementation(libs.decompose)
        implementation(libs.koin.core)
    }
}
