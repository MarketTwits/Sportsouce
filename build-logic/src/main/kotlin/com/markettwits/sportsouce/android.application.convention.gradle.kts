import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.markettwits.sportsouce.extensions.loadProperties
import com.markettwits.sportsouce.extensions.propertyDecodedString
import com.markettwits.sportsouce.extensions.propertyString
import com.markettwits.sportsouce.sources.ApkConfig
import ru.ok.tracer.mapping_plugin.TracerConfig

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("ru.ok.tracer")
}

configure<BaseExtension> {
    commonAndroid(project)
}

android {
    namespace = ApkConfig.APPLICATION_ID
    configureSigning()
    configureBuildTypes()
    configureOutputFileNames()
}

tracer {
    configureTracer()
}

configureBundleOutputNames()

private fun BaseAppModuleExtension.configureSigning() {
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

private fun BaseAppModuleExtension.configureBuildTypes() {
    val releaseSigning = signingConfigs.findByName("release")

    buildTypes {
        defaultConfig {
            androidResources.localeFilters += listOf("en", "ru")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
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
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

private fun BaseAppModuleExtension.configureOutputFileNames() {
    applicationVariants.all {
        val variant = this
        variant.outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val outputFileName = "Sportsauce-${variant.versionName}-${variant.versionCode}.apk"
                output.outputFileName = outputFileName
            }
    }
}

private fun Project.configureBundleOutputNames() {
    val projectBuildDir = layout.buildDirectory
    val projectAndroid = extensions.getByType<BaseAppModuleExtension>()

    afterEvaluate {
        tasks.matching { it.name.contains("bundle") && it.name.contains("Release") }.configureEach {
            val versionCode = projectAndroid.defaultConfig.versionCode
            val versionName = projectAndroid.defaultConfig.versionName

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

private fun NamedDomainObjectContainer<TracerConfig>.configureTracer() {
    val secretsProps = rootProject.loadProperties("secrets.properties")

    val applicationToken = secretsProps.propertyString("tracer.application.token")
    val pluginToken = secretsProps.propertyString("tracer.plugin.token")

    create("defaultConfig") {
        this.pluginToken = pluginToken
        this.appToken = applicationToken
    }
}


