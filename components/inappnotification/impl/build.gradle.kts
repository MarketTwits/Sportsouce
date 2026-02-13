plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}


kotlin {

    android {
        namespace = "com.markettwits.inappnotification.impl"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.core.ui)
            implementation(projects.components.inappnotification.api)
        }
    }
}
