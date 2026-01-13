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
            implementation(libs.kotlinx.serialization.json)
            implementation(compose.components.resources)
        }
    }
}
