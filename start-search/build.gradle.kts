plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.realm)
}

android {
    namespace = "com.markettwits.start_search"
}

dependencies {
    compileOnly(libs.kstore.file)
    compileOnly(libs.kstore)
    implementation(libs.realm)
    implementation(projects.cache)
    implementation(projects.cloud)
    implementation(projects.coreUi)
    implementation(projects.coreKoin)
    implementation(projects.start)
    implementation(projects.review.startFilter)
    implementation(projects.startsCommon)
    implementation(libs.bundles.mviKotlin)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.koin.core)
    implementation(libs.material3.html.text)
}