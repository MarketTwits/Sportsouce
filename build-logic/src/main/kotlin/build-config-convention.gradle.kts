import java.util.Properties

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.github.gmazzo.buildconfig")
}

buildConfig {

    val version = localLibs.findVersion("versionName").get().toString()

    val secretKeyProperties by lazy {
        val secretKeyPropertiesFile = rootProject.file("secrets.properties")
        Properties().apply { secretKeyPropertiesFile.inputStream().use { secret -> load(secret) } }
    }

    val apiProdPath = secretKeyProperties["sportsauce.api.prod.path"]?.toString() ?: error(
        exceptionMessage("sportsauce.api.prod.path")
    )

    val apiDevPath = secretKeyProperties["sportsauce.api.dev.path"]?.toString() ?: error(
        exceptionMessage("sportsauce.api.dev.path")
    )

    generateAtSync = false

    buildConfigField("APP_VERSION", version)
    buildConfigField("APP_VERSION_NUMBER", versionCode(version))
    buildConfigField("BUILD_TIME", System.currentTimeMillis())
    buildConfigField("SPORTSAUCE_API_PATH", apiProdPath)

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

fun exceptionMessage(propertyName: String) =
    "You should provide $propertyName into gradle.properties"