plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.schedule"
}
dependencies {
    implementation(project(":cloud"))
    implementation(project(":core-ui"))
    implementation(project(":core-koin"))
    implementation(project(":starts"))
    implementation(project(":start"))
    implementation(libs.koin.core)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.mviKotlin)
}
