import com.markettwits.sportsouce.extensions.PROJECT_VERSION_CODE
import com.markettwits.sportsouce.extensions.PROJECT_VERSION_NAME
import java.util.*

private val apiPathPropertiesProd = "com.sportsauce.api.path.prod"
private val apiPathPropertiesDev = "com.sportsauce.api.path.dev"
private val apiPathPropertiesIsDev = "com.sportsauce.api.isdev"

plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.build.konfig)
}

android {
    namespace = "com.markettwits.buildkonfig"
    buildFeatures.buildConfig = true
}

buildConfig {
    className("BuildKonfig")

    packageName("${android.namespace}")

    useKotlinOutput { internalVisibility = false }

    val versionName = project.PROJECT_VERSION_NAME
    val versionCode = project.PROJECT_VERSION_CODE

    val secretKeyProperties by lazy {
        val secretKeyPropertiesFile = rootProject.file("secrets.properties")
        Properties().apply { secretKeyPropertiesFile.inputStream().use { secret -> load(secret) } }
    }

    val isDev = providers.gradleProperty(apiPathPropertiesIsDev).get().toBoolean()

    val apiPath = if (isDev)
        secretKeyProperties[apiPathPropertiesDev]?.toString()
    else
        secretKeyProperties[apiPathPropertiesProd]?.toString()
    ?: throw IllegalStateException("No api path found")
    generateAtSync = false
    buildConfigField("APP_VERSION", versionName)
    buildConfigField("APP_VERSION_NUMBER", versionCode)
    buildConfigField("BUILD_TIME", System.currentTimeMillis())
    buildConfigField("SPORTSAUCE_API_PATH", apiPath)
}