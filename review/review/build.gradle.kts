import com.android.tools.r8.internal.js

plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.review"
}
dependencies {
    api(project(":cloud"))
    implementation(project(":auth"))
    implementation(project(":news"))
    implementation(project(":start"))
    implementation(project(":starts"))
    implementation(project(":review:start-filter"))
    implementation(project(":review:random"))
    implementation(project(":review:schedule"))
    implementation(project(":core-ui"))
    implementation(project(":core-koin"))
    implementation(libs.koin.core)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.mviKotlin)
}
