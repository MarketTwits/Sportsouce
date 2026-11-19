plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets.androidMain.dependencies {
        implementation(libs.androidx.core)
    }

    sourceSets.commonTest.dependencies {
        implementation(libs.kotlin.test)
        implementation(libs.kotlinx.coroutines.test)
        implementation("io.ktor:ktor-client-mock:${libs.versions.ktor.get()}")
    }


    android {
        namespace = "com.markettwits.unauthorized"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.auth.authService)
            implementation(projects.components.core.cache)
            implementation(projects.components.core.cloud)
            implementation(projects.components.core.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.koin.core)
            implementation(libs.bundles.decompose.compose)
            implementation(libs.bundles.mviKotlin)
        }
    }
}
