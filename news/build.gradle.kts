plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.news"
}
dependencies {
    implementation(project(":cloud"))
    implementation(project(":core-ui"))
    implementation ("de.charlex.compose.material3:material3-html-text:2.0.0-beta01")
    implementation(projects.core.time)
    implementation(libs.koin.core)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.mviKotlin)
}
