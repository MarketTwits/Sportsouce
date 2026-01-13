plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {

    android {
        namespace = "com.markettwits.selfupdater.unknown"
    }

    sourceSets.commonMain.dependencies {
        implementation(projects.components.selfupdater.api)
    }
}
