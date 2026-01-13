plugins {
    alias(libs.plugins.kotlin.kmp.convention)
}


kotlin {

    android {
        namespace = "com.markettwits.selfupdater.version"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(projects.components.core.buildConfig)
        }
    }
}
