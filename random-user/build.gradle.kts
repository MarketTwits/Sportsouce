plugins {
    alias(libs.plugins.kotlin.jvm.convention)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}