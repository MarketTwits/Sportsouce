plugins {
    id("com.android.library")
    id("base-android-convention")
    id("org.jetbrains.kotlin.multiplatform")
}

kotlin {
    jvm()

    jvmToolchain(localLibs.findVersion("jvm-dot").get().toString().toInt())
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = localLibs.findVersion("jvm-dot").get().toString()
            }
        }
    }
}
