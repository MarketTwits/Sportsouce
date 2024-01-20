@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.root"

}

dependencies {

    api(project(":cloud"))
    implementation(project(":start"))
    implementation(project(":starts"))
    implementation(project(":profile:profile"))
    implementation(project(":news"))
    implementation(project(":review:review"))
    implementation(project(":core-ui"))
    implementation(project(":auth"))
    implementation(libs.bundles.mviKotlin)
    api(libs.bundles.composeUiBundle)
    api(libs.junit.ext.ktx)
    debugApi(libs.bundles.composeUiBundleDebug)
    implementation(libs.decompose)
    //implementation(libs.decompose.compose.jetbrains)
    implementation(libs.decompose.compose.extension)
    implementation ("de.charlex.compose.material3:material3-html-text:2.0.0-beta01")
    implementation(libs.decompose.android)
    implementation(libs.kotlinx.datetime)
}