import com.android.build.gradle.BaseExtension
import extensions.PROJECT_JAVA_VERSION

plugins {
    id("com.android.application")
    id("kotlin-android")
}

configure<BaseExtension> {
    commonAndroid(project)
}

kotlin{
    jvmToolchain(project.PROJECT_JAVA_VERSION.getMajorVersion().toInt())
}
