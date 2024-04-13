plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.compose")
}
android {
    compileSdk = localLibs.findVersion("compileSdk").get().toString().toInt()
    defaultConfig {
        minSdk = localLibs.findVersion("minSdk").get().toString().toInt()
    }
}
kotlin {
    jvm()
}
