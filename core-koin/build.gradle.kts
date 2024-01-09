plugins {
    //alias(libs.plugins.android.library.compose.convention)
    id("android-library-convention")
    alias(libs.plugins.kotlin.serialization)

}

android {
    namespace = "com.markettwits.core_koin"
}
dependencies {
    implementation(libs.decompose)
    implementation(libs.mvikotlin)
    implementation(libs.koin.core)
}