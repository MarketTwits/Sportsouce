package sources

//import gradle.kotlin.dsl.accessors._44d381bab6f9721b63bbec4cdf480a90.implementation

data class GradlePluginLibrary(
    val module: String,
    val version: String
) {
    val plugin: String = "$module:$version"
}

object GradlePlugins {

    val TRACER = GradlePluginLibrary(
        "ru.ok.tracer:ru.ok.tracer.gradle.plugin",
        "0.2.15"
    )
    val VK_COMPOSE = GradlePluginLibrary(
        "com.vk.vkompose:gradle-plugin",
        "0.4.2"
    )
    val GET_BRAINS_COMPOSE = GradlePluginLibrary(
        "org.jetbrains.compose:org.jetbrains.compose.gradle.plugin",
        "1.6.10"
    )
    val KOTLIN_KMP = GradlePluginLibrary(
        "org.jetbrains.kotlin.multiplatform:org.jetbrains.kotlin.multiplatform.gradle.plugin",
        "2.0.0"
    )
    val KOTLIN_COMPOSE_PLUGIN = GradlePluginLibrary(
        "org.jetbrains.kotlin.plugin.compose:org.jetbrains.kotlin.plugin.compose.gradle.plugin",
        "2.0.0"
    )
    val BUILD_CONFIG = GradlePluginLibrary(
        "com.github.gmazzo.buildconfig:plugin",
        "5.3.5"
    )
}

//fun DependencyHandler.gradlePlugins(){
//    implementation(GradlePlugins.TRACER)
//    implementation(GradlePlugins.VK_COMPOSE)
//    implementation(GradlePlugins.GET_BRAINS_COMPOSE)
//    implementation(GradlePlugins.KOTLIN_KMP)
//    implementation(GradlePlugins.KOTLIN_COMPOSE_PLUGIN)
//    implementation(GradlePlugins.BUILD_CONFIG)
//}
