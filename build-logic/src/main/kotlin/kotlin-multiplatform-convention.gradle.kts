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

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.addAll(suppressException())
    }
}

fun suppressException(): List<String> = listOf(
    "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
    "-Xuse-experimental=kotlinx.coroutines.ObsoleteCoroutinesApi"
)
