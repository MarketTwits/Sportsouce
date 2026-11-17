plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}
kotlin {

    android {
        namespace = "org.markettwits.core.errors"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.core.ui)
            implementation(projects.components.core.cloud)
            implementation(projects.components.core.log)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.compose.components.resources)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation("io.ktor:ktor-client-mock:${libs.versions.ktor.get()}")
        }
    }
}
