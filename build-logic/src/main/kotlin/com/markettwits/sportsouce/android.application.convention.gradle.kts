import com.android.build.api.dsl.ApplicationExtension
import com.markettwits.sportsouce.extensions.loadProperties
import com.markettwits.sportsouce.extensions.propertyDecodedString
import com.markettwits.sportsouce.extensions.propertyString
import com.markettwits.sportsouce.sources.ApkConfig
import ru.ok.tracer.mapping_plugin.TracerConfig

plugins {
    id("com.android.application")
    id("ru.ok.tracer")
}

configure<ApplicationExtension> {
    commonAndroid(project)
    namespace = ApkConfig.APPLICATION_ID
    configureSigning()
    configureBuildTypes()
}

tracer {
    configureTracer()
}

configureApkOutputNames()
configureBundleOutputNames()

private fun ApplicationExtension.configureSigning() {
    val keystoreFile = rootProject.file("key-store.jks")
    val secretsFile = rootProject.file("secrets.properties")

    if (!keystoreFile.exists() || !secretsFile.exists()) {
        project.logger.lifecycle(
            "Release signing config is skipped: key-store.jks or secrets.properties was not found."
        )
        return
    }

    val secretsProps = rootProject.loadProperties("secrets.properties")

    signingConfigs {
        if (findByName("release") == null) {
            create("release") {
                storeFile = keystoreFile
                storePassword = secretsProps.propertyDecodedString("storePassword64")
                keyAlias = secretsProps.propertyDecodedString("keyAlias64")
                keyPassword = secretsProps.propertyDecodedString("keyPassword64")
            }
        }
    }
}

private fun NamedDomainObjectContainer<TracerConfig>.configureTracer() {
    val secretsProps = rootProject.loadProperties("secrets.properties")

    val applicationToken = secretsProps.propertyString("tracer.application.token")
    val pluginToken = secretsProps.propertyString("tracer.plugin.token")

    create("defaultConfig") {
        this.pluginToken = pluginToken
        this.appToken = applicationToken
    }
}

private fun ApplicationExtension.configureBuildTypes() {
    val releaseSigning = signingConfigs.findByName("release")

    androidResources {
        localeFilters.addAll(listOf("en", "ru"))
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (releaseSigning != null) {
                signingConfig = releaseSigning
            }
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

private fun Project.configureBundleOutputNames() {
    val projectBuildDir = layout.buildDirectory
    val projectAndroid = extensions.getByType<ApplicationExtension>()
    val versionCode = projectAndroid.defaultConfig.versionCode
    val versionName = projectAndroid.defaultConfig.versionName

    afterEvaluate {
        tasks.matching { it.name.contains("bundle") && it.name.contains("Release") }.configureEach {
            doLast {
                val bundleDir = projectBuildDir.get().asFile.resolve("outputs/bundle/release")
                if (bundleDir.exists()) {
                    bundleDir.listFiles()?.forEach { file ->
                        if (file.extension == "aab") {
                            val newName = "Sportsauce-${versionName}-${versionCode}.aab"
                            val newFile = File(bundleDir, newName)
                            if (file != newFile) {
                                file.renameTo(newFile)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun Project.configureApkOutputNames() {
    val projectAndroid = extensions.getByType<ApplicationExtension>()
    val versionCode = projectAndroid.defaultConfig.versionCode ?: return
    val versionName = projectAndroid.defaultConfig.versionName ?: return

    @Suppress("UNCHECKED_CAST")
    val buildOutputs =
        extensions.getByName("buildOutputs") as NamedDomainObjectContainer<com.android.build.gradle.api.BaseVariantOutput>

    buildOutputs.configureEach {
        if (this is com.android.build.gradle.api.ApkVariantOutput) {
            outputFileName = "Sportsauce-$versionName-$versionCode.apk"
        }
    }
}
