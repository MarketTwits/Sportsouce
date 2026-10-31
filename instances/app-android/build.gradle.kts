plugins {
    id("android.application.convention")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    flavorDimensions += "distribution"
    productFlavors {
        create("googlePlay") { dimension = "distribution" }
        create("github") { dimension = "distribution" }
    }
}

dependencies {
    implementation(projects.components.core.theme)
    implementation(projects.components.core.ui)
    implementation(projects.components.root)
    implementation(projects.components.core.cache)
    implementation(projects.components.analytics.crashlytics)
    implementation(projects.components.core.activityholder)
    implementation(projects.components.core.log)
    implementation(projects.components.deeplink.api)
    implementation(projects.components.deeplink.impl)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.compose.activity)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.tracer.crash.report)
    implementation(libs.koin.android)
    implementation(projects.components.core.koin)
    implementation(projects.components.selfupdater.thirdparty.api)
    add("githubImplementation", projects.components.selfupdater.api)
    add("githubImplementation", projects.components.inappnotification.api)
}
