plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}
kotlin {

    android {
        namespace = "com.markettwits.selfupdater.thirdparty.api"
    }

    sourceSets {
        androidMain.dependencies {
            implementation(projects.components.selfupdater.googleplay)
        }
        commonMain.dependencies {
            implementation(projects.components.selfupdater.api)
            implementation(projects.components.inappnotification.api)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.koin.core)
        }
        jvmMain.dependencies {
            implementation(projects.components.selfupdater.unknown)
        }
        jsMain.dependencies {
            implementation(projects.components.selfupdater.unknown)
        }
    }
}
