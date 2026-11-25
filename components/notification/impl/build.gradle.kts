plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "com.markettwits.notification.impl"
        androidResources { enable = true }
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.notification.api)
            implementation(projects.components.starts.startsCommon)
            implementation(projects.components.auth.authFlow)
            implementation(projects.components.auth.authService)
            implementation(projects.components.core.cloud)
            implementation(projects.components.core.errors)
            implementation(projects.components.core.ui)
            implementation(projects.components.core.paging)
            implementation(projects.components.core.koin)
            implementation(projects.components.core.log)
            implementation(projects.components.core.intent.composable)
            implementation(projects.components.core.intent.api)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.bundles.decompose.compose)
            implementation(libs.bundles.mviKotlin)
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(libs.androidx.core)
            implementation(libs.compose.activity)
            implementation(libs.firebase.messaging)
            implementation(libs.koin.android)
            implementation(libs.kotlinx.coroutines.play.services)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation("io.ktor:ktor-client-mock:${libs.versions.ktor.get()}")
        }
    }
}
