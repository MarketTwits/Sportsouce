import groovy.json.JsonOutput
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

abstract class GenerateWebDeploymentMetadata : DefaultTask() {
    @get:Input abstract val sequence: Property<Long>
    @get:Input abstract val versionName: Property<String>
    @get:Input abstract val buildNumber: Property<Long>
    @get:InputDirectory @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val notesDirectory: DirectoryProperty
    @get:OutputDirectory abstract val destination: DirectoryProperty

    @TaskAction
    fun generate() {
        val changes = mapOf("ru" to "ru-RU", "en" to "en-US").mapValues { (_, locale) ->
            notesDirectory.file("whatsnew-$locale").get().asFile.readText().replace("\$APP_VERSION", versionName.get())
        }
        val metadata = mapOf(
            "schemaVersion" to 1, "sequence" to sequence.get(),
            "versionName" to versionName.get(), "buildNumber" to buildNumber.get(), "changes" to changes,
        )
        destination.get().asFile.apply { mkdirs() }.resolve("version.json")
            .writeText(JsonOutput.toJson(metadata), Charsets.UTF_8)
    }
}

val generateWebDeploymentMetadata = tasks.register<GenerateWebDeploymentMetadata>("generateWebDeploymentMetadata") {
    sequence.set(providers.environmentVariable("SPORTSAUCE_WEB_DEPLOYMENT_SEQUENCE").orElse("0").map(String::toLong))
    versionName.set(libs.versions.versionName)
    buildNumber.set(libs.versions.versionCode.map(String::toLong))
    notesDirectory.set(rootProject.layout.projectDirectory.dir(".github/whatsnew"))
    destination.set(layout.buildDirectory.dir("generated/webDeployment"))
}

kotlin {
    js(IR) {
        useEsModules()
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    sourceSets.jsMain { resources.srcDir(generateWebDeploymentMetadata) }

    sourceSets.jsMain.dependencies {
        implementation(projects.components.core.theme)
        implementation(projects.components.core.time)
        implementation(projects.components.core.ui)
        implementation(projects.components.root)
        implementation(projects.components.deeplink.api)
        implementation(projects.components.core.cache)
        implementation(libs.compose.components.resources)
        implementation(libs.bundles.decompose.compose)
        implementation(libs.koin.core)
        implementation(projects.components.core.koin)
        implementation(project.dependencies.enforcedPlatform(libs.jetbrains.kotlinWrappers.kotlinWrappersBom))
        implementation(libs.kotlin.browser)
    }
}

tasks.withType<KotlinWebpack>().configureEach {
    mode = when {
        name.contains("development", ignoreCase = true) -> KotlinWebpackConfig.Mode.DEVELOPMENT
        name.contains("production", ignoreCase = true) ||
                name.contains("distribution", ignoreCase = true) -> KotlinWebpackConfig.Mode.PRODUCTION

        else -> mode
    }
}
