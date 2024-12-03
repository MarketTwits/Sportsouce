import java.util.Properties

plugins {
    id("com.android.application") apply false
    id("ru.ok.tracer")
}

fetchTracerToken().apply()

fun fetchTracerToken(): TracerConfig {

    val secretKeyProperties by lazy {
        val secretKeyPropertiesFile = rootProject.file("secrets.properties")
        Properties().apply { secretKeyPropertiesFile.inputStream().use { secret -> load(secret) } }
    }

    val applicationToken = secretKeyProperties["tracer.application.token"]?.toString() ?: error(
        exceptionMessage("tracer.application.token")
    )

    val pluginToken = secretKeyProperties["tracer.plugin.token"]?.toString() ?: error(
        exceptionMessage("tracer.application.token")
    )

    return TracerConfig(applicationToken, pluginToken)
}

data class TracerConfig(
    private val applicationToken: String,
    private val applicationPluginToken: String
) {
    fun apply() {
        tracer {
            create("defaultConfig") {
                pluginToken = applicationPluginToken
                appToken = applicationToken
            }
        }
    }
}

fun exceptionMessage(propertyName: String) =
    "You should provide $propertyName into gradle.properties"