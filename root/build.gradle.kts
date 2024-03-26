@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.root"

}

dependencies {
    implementation(project(":starts:starts"))
    implementation(project(":profile:profile"))
    implementation(project(":review:review"))
    implementation(project(":core-ui"))
    implementation(libs.bundles.composeUiBundle)
    implementation(libs.bundles.decompose.compose)
}