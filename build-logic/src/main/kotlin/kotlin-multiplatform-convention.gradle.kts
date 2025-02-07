import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("com.android.library")
    id("base-android-convention")
    id("org.jetbrains.kotlin.multiplatform")
}

kotlin {
    jvm()
    jvmToolchain(localLibs.findVersion("jvm-dot").get().toString().toInt())
    androidTarget()

    js{
        browser()
        nodejs()
        binaries.executable()
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
       // freeCompilerArgs.addAll(suppressException())
    }

}