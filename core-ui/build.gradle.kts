
plugins {
    alias(libs.plugins.kotlin.jvm.convention)
}
dependencies {
    implementation(libs.decompose)
    implementation(libs.kotlinx.coroutines.core)
}