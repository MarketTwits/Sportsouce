import com.android.build.gradle.BaseExtension
import com.markettwits.sportsouce.extensions.suppressExperimentalCoroutinesApi

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
}

configure<BaseExtension> {
    commonAndroid(project)
    commonJava(project)
}

kotlin {
    jvm()
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