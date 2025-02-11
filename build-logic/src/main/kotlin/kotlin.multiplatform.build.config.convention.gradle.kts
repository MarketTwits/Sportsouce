import extensions.PROJECT_VERSION_CODE
import extensions.PROJECT_VERSION_NAME
import java.util.Properties

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.github.gmazzo.buildconfig")
}

buildConfig {

    val versionName = project.PROJECT_VERSION_NAME
    val versionCode = project.PROJECT_VERSION_CODE

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

    buildConfigField("APP_VERSION", versionName)
    buildConfigField("APP_VERSION_NUMBER", versionCode)
    buildConfigField("BUILD_TIME", System.currentTimeMillis())
    buildConfigField("SPORTSAUCE_API_PATH", apiProdPath)

}

fun exceptionMessage(propertyName: String) =
    "You should provide $propertyName into gradle.properties"