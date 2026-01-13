plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}


kotlin {

    android {
        namespace = "com.markettwits.deeplink.impl"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.deeplink.api)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(libs.appcompat)
        }
    }
}
