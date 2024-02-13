plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.review"
}
dependencies {
    compileOnly(libs.kstore)
    compileOnly(libs.kstore.file)
    implementation(projects.cache)
    implementation(projects.cloud)
    implementation(projects.news)
    implementation(projects.start)
    implementation(projects.review.startFilter)
    implementation(projects.review.random)
    implementation(projects.review.schedule)
    implementation(projects.review.popular)
    implementation(projects.coreUi)
    implementation(projects.coreKoin)
    implementation(projects.startsCommon)
    implementation(libs.koin.core)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.mviKotlin)
}
