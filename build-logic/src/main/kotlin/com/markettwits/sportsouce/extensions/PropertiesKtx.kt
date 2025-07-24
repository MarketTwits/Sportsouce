package com.markettwits.sportsouce.extensions

import org.gradle.api.Project
import java.io.File
import java.util.*

fun Properties.propertyInt(key: String): Int {
    val property = getProperty(key)

    if (property.isNullOrEmpty()) {
        error("property $key is null")
    }

    return try {
        property.toInt()
    } catch (exception: NumberFormatException) {
        error("Cast exception for $key")
    }
}

fun Properties.propertyString(key: String): String {
    val property = getProperty(key)

    if (property.isNullOrEmpty()) {
        error("property $key is null")
    }

    return property.toString()
}

fun Properties.propertyInt(key: String, default: Int): Int {
    return getProperty(key)?.toIntOrNull() ?: default
}

fun Properties.propertyString(key: String, default: String): String {
    return getProperty(key) ?: default
}

fun Properties.propertyDecodedString(key: String): String {
    val encodedValue = propertyString(key)
    return try {
        String(Base64.getDecoder().decode(encodedValue))
    } catch (e: IllegalArgumentException) {
        // Если строка не в Base64, возвращаем как есть (для обратной совместимости)
        encodedValue
    }
}

fun Properties.propertyDecodedString(key: String, default: String): String {
    val encodedValue = propertyString(key, default)
    return try {
        String(Base64.getDecoder().decode(encodedValue))
    } catch (e: IllegalArgumentException) {
        encodedValue
    }
}

fun Properties.propertyFile(project: Project, key: String): File {
    val filePath = propertyString(key)
    val file = project.file("${project.rootDir}/${filePath}")
    if (file.exists()) {
        return file
    } else {
        error("File not found at path: $filePath")
    }
}

fun Properties.propertyFile(project: Project, key: String, default: String): File {
    val filePath = propertyString(key, default)
    val file = project.file("${project.rootDir}/${filePath}")
    if (file.exists()) {
        return file
    } else {
        error("File not found at path: $filePath")
    }
}

fun Project.loadFile(path: String): File {
    val file = file("${rootDir}/${path}")
    if (file.exists()) {
        return file
    } else {
        error("Keystore file dont found in $path")
    }
}

fun Project.loadProperties(path: String): Properties {
    val props = Properties()
    val file = file("${rootDir}/${path}")
    if (file.exists()) {
        file.inputStream().use { props.load(it) }
    } else {
        error("Properties file not found at $file")
    }
    return props
}
