import com.android.build.gradle.BaseExtension
import extensions.PROJECT_JAVA_VERSION
import gradle.kotlin.dsl.accessors._c0ebf38a9e0e766e40379ba7eaa32ea4.kotlin

plugins {
    id("com.android.library")
    id("kotlin-android")
}

configure<BaseExtension> {
   commonAndroid(project)
}

kotlin{
    jvmToolchain(project.PROJECT_JAVA_VERSION.getMajorVersion().toInt())
}

