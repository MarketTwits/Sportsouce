plugins {
    //alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.unauthorized"
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.cloud)
            implementation(projects.auth.authService)
            implementation(projects.core.ui)
            implementation(compose.components.resources)
            implementation(libs.koin.core)
            implementation("io.github.alexzhirkevich:compottie:1.1.2")
            implementation(libs.bundles.decompose.compose)
            implementation(libs.bundles.mviKotlin)
        }
    }
}


//dependencies {
//    implementation(projects.cloud)
//    implementation(projects.auth.authService)
//    implementation(projects.core.ui)
//    implementation(libs.koin.core)
// //  implementation(libs.lottie)
//    implementation("io.github.alexzhirkevich:compottie:1.1.2")
//    implementation(libs.bundles.decompose.compose)
//    implementation(libs.bundles.mviKotlin)
//}