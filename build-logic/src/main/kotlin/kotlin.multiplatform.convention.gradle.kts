import com.android.build.gradle.BaseExtension
import extensions.PROJECT_JAVA_VERSION
import extensions.suppressExperimentalCoroutinesApi

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
}

configure<BaseExtension> {
    commonAndroid(project)
}

kotlin {
    jvm()
    jvmToolchain(project.PROJECT_JAVA_VERSION.getMajorVersion().toInt())
    androidTarget()
    js {
        browser()
        nodejs()
        binaries.executable()
    }

    compilerOptions {
        freeCompilerArgs.addAll(suppressExperimentalCoroutinesApi())
    }

}