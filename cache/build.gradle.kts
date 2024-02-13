plugins {
    alias(libs.plugins.kotlin.jvm.convention)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.realm)
}

dependencies {
    implementation(project(":core-koin"))
    implementation(libs.realm)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kstore)
    implementation(libs.kstore.file)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.koin.core)
}