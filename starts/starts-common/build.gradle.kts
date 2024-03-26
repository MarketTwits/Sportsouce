plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.starts_common"
}
dependencies {

    compileOnly(projects.cloud)
    implementation(projects.cache)
    implementation(projects.coreUi)
    implementation(libs.material3.html.text)
    implementation(libs.kotlinx.serialization.core)
}