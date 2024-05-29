
plugins {

    //  alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}

android.namespace = "com.flipperdevices.inappnotification.impl"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.ui)
            implementation(projects.inappnotification.api)
        }
    }
}

//dependencies {
//    implementation(projects.inappnotification.api)
//    implementation(libs.bundles.composeUiBundle)
//    implementation(projects.core.ui)
//}
