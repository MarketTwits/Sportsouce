import com.android.build.gradle.BaseExtension
import extensions.PROJECT_JAVA_VERSION

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
}