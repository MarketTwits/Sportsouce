plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
    maven("https://plugins.gradle.org/m2/")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven { setUrl("https://artifactory-external.vkpartner.ru/artifactory/maven/") }
}

dependencies {
    implementation(libs.android.gradle)
    implementation(libs.kotlin.gradle)
    implementation(libs.tracer.gradle.plugin)
    implementation(libs.org.jetbrains.compose.gradle.plugin)
    implementation(libs.org.jetbrains.kotlin.multiplatform.gradle.plugin)
    implementation(libs.org.jetbrains.kotlin.plugin.compose.gradle.plugin)
    implementation(libs.buildConfigPlugin)
}