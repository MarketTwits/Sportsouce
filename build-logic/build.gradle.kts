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
    implementation("ru.ok.tracer:ru.ok.tracer.gradle.plugin:0.2.15")
    implementation("com.vk.vkompose:gradle-plugin:0.4.2")
    implementation("org.jetbrains.compose:compose-gradle-plugin:1.6.1")
    implementation("org.jetbrains.kotlin.multiplatform:org.jetbrains.kotlin.multiplatform.gradle.plugin:1.9.23")
//    implementation("org.jetbrains.compose:org.jetbrains.compose:1.6.1")
//    implementation("org.jetbrains.kotlin.multiplatform:org.jetbrains.kotlin.multiplatform:1.6.0")
}
