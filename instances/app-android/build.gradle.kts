plugins {
    id("android.application.convention")
    id("org.jetbrains.kotlin.plugin.compose")
}

dependencies {
    implementation(projects.components.core.theme)
    implementation(projects.components.core.ui)
    implementation(projects.components.root)
    implementation(projects.components.core.cache)
    implementation(projects.components.analytics.crashlytics)
    implementation(projects.components.core.activityholder)
    implementation(projects.components.deeplink.api)
    implementation(projects.components.deeplink.impl)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.compose.activity)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.tracer.crash.report)
    implementation(libs.koin.android)
    implementation(projects.components.core.koin)
}
