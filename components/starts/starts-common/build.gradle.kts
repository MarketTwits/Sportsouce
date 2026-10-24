plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}
kotlin {

    android {
        namespace = "com.markettwits.starts.common"
    }

    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation("io.ktor:ktor-client-mock:${libs.versions.ktor.get()}")
            implementation(projects.components.core.cloud)
        }
        commonMain.dependencies {
            implementation(projects.components.core.buildConfig)
            implementation(projects.components.core.cache)
            implementation(projects.components.core.ui)
            implementation(projects.components.core.time)
            api(projects.components.core.paging)
            implementation(projects.components.starts.startsCloud)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
