plugins {
    id("com.android.library")
    id("base-android-convention")
    id("org.jetbrains.kotlin.multiplatform")
}
kotlin {
    // jvm()
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
}
android {
    compileSdk = localLibs.findVersion("compileSdk").get().toString().toInt()
    defaultConfig {
        minSdk = localLibs.findVersion("minSdk").get().toString().toInt()
    }
}
