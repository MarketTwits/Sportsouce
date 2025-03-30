package com.markettwits.sportsouce.extensions

import org.gradle.api.JavaVersion
import org.gradle.api.Project

val Project.PROJECT_JAVA_VERSION: JavaVersion
    get() = JavaVersion.toVersion(libs.versions.jvm.get())

val Project.PROJECT_VERSION_NAME: String
    get() = libs.versions.versionName.get()

val Project.PROJECT_VERSION_CODE: Int
    get() = versionCode(PROJECT_VERSION_NAME)

private fun versionCode(versionName: String): Int {
    val components = versionName.split(".")

    val major = components.getOrNull(0)?.toIntOrNull()
        ?: throw IllegalArgumentException("major version in version name not found")
    val minor = components.getOrNull(1)?.toIntOrNull()
        ?: throw IllegalArgumentException("minor version in version name not found")
    val patch = components.getOrNull(2)?.toIntOrNull()
        ?: throw IllegalArgumentException("patch version in version name not found")

    return major * 10000 + minor * 100 + patch
}