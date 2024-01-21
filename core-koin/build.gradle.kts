plugins {
    alias(libs.plugins.kotlin.jvm.convention)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.decompose)
    implementation(libs.mvikotlin)
    implementation(libs.koin.core)
}