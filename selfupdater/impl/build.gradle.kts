plugins {
    alias(libs.plugins.kotlin.kmp.convention)
}
android{
    namespace = "com.markettwits.selfupdater.impl"
}
kotlin{
    sourceSets{
        commonMain.dependencies {
            implementation(projects.selfupdater.api)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}