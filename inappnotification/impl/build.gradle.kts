plugins {

    alias(libs.plugins.android.library.compose.convention)
}

android.namespace = "com.flipperdevices.inappnotification.impl"

dependencies {
    implementation(projects.inappnotification.api)
    implementation(libs.bundles.composeUiBundle)
    implementation(projects.coreUi)
}
