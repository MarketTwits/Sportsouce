plugins {
    id("com.android.application") apply false
    id("ru.ok.tracer")
}

fetchTracerToken().apply()

fun fetchTracerToken(): TracerConfig {

    val applicationToken = property("tracerApplicationToken")?.toString() ?: error(
        exceptionMessage("tracerApplicationToken")
    )
    val pluginToken = property("tracerPluginToken")?.toString() ?: error(
        exceptionMessage("tracerPluginToken")
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
                pluginToken = "JlDv2pDQlHq4qbZPeKTv66E5zF8Zpvtz39FzTpEmdgY1"
                appToken = "ylXqrrPexa5weKlB4sAHYteLVnvqI5SQYx4Aghh6GKr"
            }
        }
    }
}

fun exceptionMessage(propertyName: String) =
    "You should provide $propertyName into gradle.properties"