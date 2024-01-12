import com.android.tools.r8.internal.js

plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.random"
}
dependencies {
    implementation(project(":cloud"))
    implementation(project(":core-ui"))
    implementation(project(":core-koin"))
    implementation(libs.koin.core)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.mviKotlin)
}
