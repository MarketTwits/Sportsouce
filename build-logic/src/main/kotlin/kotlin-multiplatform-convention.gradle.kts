plugins {
    id("com.android.library")
    id("base-android-convention")
    id("org.jetbrains.kotlin.multiplatform")
}

kotlin {
    jvm()
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = localLibs.findVersion("jvm-dot").get().toString()
            }
        }
    }
}
