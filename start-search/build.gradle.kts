plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.start_search"
}

dependencies {
    implementation(project(":cloud"))
    implementation(project(":core-ui"))
    implementation(project(":core-koin"))
    implementation(projects.start)
    //implementation(projects.review.startFilter)
    implementation(projects.startsCommon)
    implementation(libs.bundles.mviKotlin)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.koin.core)
    implementation(libs.material3.html.text)
}