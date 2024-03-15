plugins {
    alias(libs.plugins.kotlin.jvm.convention)
    // alias(libs.plugins.realm)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(projects.cloud)
    implementation(projects.cache)
    implementation(libs.koin.core)
    //  implementation(libs.realm)
}