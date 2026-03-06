plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {

    android {
        namespace = "com.markettwits.starts.recent"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.core.cache)
            implementation(projects.components.starts.startsCommon)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
