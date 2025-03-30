import java.util.Properties

plugins {
    id("com.android.application") apply false
    id("ru.ok.tracer")
}

tracer {
    val secretKeyProperties by lazy {
        val secretKeyPropertiesFile = rootProject.file("secrets.properties")
        Properties().apply { secretKeyPropertiesFile.inputStream().use { secret -> load(secret) } }
    }

    val applicationToken = secretKeyProperties["tracer.application.token"]?.toString() ?: error(
        exceptionMessage("tracer.application.token")
    )

    val applicationPluginToken = secretKeyProperties["tracer.plugin.token"]?.toString() ?: error(
        exceptionMessage("tracer.application.token")
    )

    create("defaultConfig") {
        pluginToken = applicationPluginToken
        appToken = applicationToken
    }
}
private fun exceptionMessage(propertyName: String) =
    "You should provide $propertyName into gradle.properties"