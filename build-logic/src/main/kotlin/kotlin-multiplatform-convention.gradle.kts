plugins {
    id("com.android.library")
    id("base-android-convention")
    id("org.jetbrains.kotlin.multiplatform")
}

kotlin {
    jvm()
    jvmToolchain(localLibs.findVersion("jvm-dot").get().toString().toInt())
    androidTarget {
        compilations.all {
            kotlinOptions {

                jvmTarget = localLibs.findVersion("jvm-dot").get().toString()

                kotlinOptions {
                    freeCompilerArgs += stabilityConfiguration()
                }
            }
        }
    }
}
fun Project.stabilityConfiguration() = listOf(
    "-P",
    "plugin:androidx.compose.compiler.plugins.kotlin:stabilityConfigurationPath=${project.rootDir.absolutePath}/compose_stability_config.conf",
)

fun Project.strongSkippingConfiguration() = listOf(
    "-P",
    "plugin:androidx.compose.compiler.plugins.kotlin:experimentalStrongSkipping=true",
)

fun Project.stableTypesReportConfiguration(): List<String> {
    val composeReportsDir = "compose_reports"
    return listOf(
        "-P",
        "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                project.layout.buildDirectory.get().dir(composeReportsDir).asFile.absolutePath,
        "-P",
        "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                project.layout.buildDirectory.get().dir(composeReportsDir).asFile.absolutePath,
    )
}
