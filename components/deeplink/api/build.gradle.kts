plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}

android.namespace = "com.markettwits.deeplink.api"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
        }
        androidMain.dependencies {
            implementation(libs.appcompat)
        }
    }
}
//commonDependencies {
//    implementation(projects.components.bridge.dao.api)
//    implementation(projects.components.core.ktx)
//    implementation(projects.components.core.kmpparcelize)
//
//    implementation(libs.kotlin.serialization.json)
//
//    implementation(libs.annotations)
//    implementation(libs.appcompat)
//}
