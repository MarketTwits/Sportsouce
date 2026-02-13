plugins {
    alias(libs.plugins.kotlin.kmp.convention)
}
kotlin {

    android {
        namespace = "com.markettwits.selfupdater.impl"
    }

    sourceSets{
        commonMain.dependencies {
            implementation(projects.components.selfupdater.api)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
