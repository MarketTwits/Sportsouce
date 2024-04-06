plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
    maven("https://plugins.gradle.org/m2/")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(libs.android.gradle)
    implementation(libs.kotlin.gradle)
    implementation("com.vk.vkompose:gradle-plugin:0.4.2")
    implementation("org.jetbrains.compose:compose-gradle-plugin:1.6.10-dev1514")

}
