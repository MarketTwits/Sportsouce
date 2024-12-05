plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.markettwits.selfupdater.unknown"
}

kotlin{
    sourceSets.commonMain.dependencies {
        implementation(projects.selfupdater.api)
    }
}