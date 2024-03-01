plugins {
    alias(libs.plugins.kotlin.jvm.convention)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(projects.coreKoin)
    implementation(projects.cloud)
    implementation(projects.cache)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.koin.core)
}