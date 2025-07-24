package com.markettwits.sportsouce.extensions

import org.gradle.api.JavaVersion
import org.gradle.api.Project

val Project.PROJECT_JAVA_VERSION: JavaVersion
    get() = JavaVersion.toVersion(libs.versions.jvm.get())

val Project.PROJECT_VERSION_NAME: String
    get() = libs.versions.versionName.get()

val Project.PROJECT_VERSION_CODE: Int
    get() = libs.versions.versionCode.get().toInt()