plugins {
    alias(libs.plugins.kotlin.kmp.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "org.markettwits.core.decompose"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            api(libs.bundles.decompose.compose)
        }
    }
}
