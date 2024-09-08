plugins {
    alias(libs.plugins.kotlin.jvm.convention)
}

dependencies {
    implementation(projects.selfupdater.api)
    implementation(libs.kotlinx.coroutines.core)
}
