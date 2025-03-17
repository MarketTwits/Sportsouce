plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.selfupdater.thirdparty.api"
}
kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(projects.selfupdater.googleplay)
        }
        commonMain.dependencies {
            implementation(projects.selfupdater.api)
            implementation(projects.inappnotification.api)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.koin.core)
        }
        jvmMain.dependencies {
            implementation(projects.selfupdater.unknown)
        }
        jsMain.dependencies {
            implementation(projects.selfupdater.unknown)
        }
    }
}