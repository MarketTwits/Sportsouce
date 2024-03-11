@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.root"

}

dependencies {

    implementation(project(":cloud"))
    implementation(project(":start:start"))
    implementation(project(":starts"))
    implementation(project(":profile:profile"))
    implementation(project(":review:review"))
    implementation(project(":core-ui"))
    implementation(libs.bundles.mviKotlin)
    implementation(libs.bundles.composeUiBundle)
    implementation(libs.junit.ext.ktx)
    implementation(libs.decompose)
    implementation(libs.decompose.compose.extension)
    implementation(libs.decompose.android)
    implementation(libs.kotlinx.datetime)
}