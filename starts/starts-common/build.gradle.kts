plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose)
}
kotlin {
    jvm()
    sourceSets {
        commonMain.dependencies {
            compileOnly(projects.cloud)
            implementation(projects.cache)
            implementation(projects.core.ui)
            implementation(libs.material3.html.text)
            implementation(projects.core.time)
            implementation(libs.kotlinx.serialization.core)
        }
    }
}
android {
    namespace = "com.markettwits.starts_common"
}