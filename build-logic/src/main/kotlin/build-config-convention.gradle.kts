plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.github.gmazzo.buildconfig")
}

buildConfig {
    val version = localLibs.findVersion("versionName").get().toString()
    generateAtSync = false
    buildConfigField("APP_VERSION", version)
    buildConfigField("APP_VERSION_NUMBER", versionCode(version))
    buildConfigField("BUILD_TIME", System.currentTimeMillis())
}

fun versionCode(versionName: String): Int {
    val components = versionName.split(".")
    val major = components.getOrNull(0)?.toIntOrNull()
        ?: throw IllegalArgumentException("major version in version name not found")
    val minor = components.getOrNull(1)?.toIntOrNull()
        ?: throw IllegalArgumentException("minor version in version name not found")
    val patch = components.getOrNull(2)?.toIntOrNull()
        ?: throw IllegalArgumentException("patch version in version name not found")
    return major * 10000 + minor * 100 + patch
}