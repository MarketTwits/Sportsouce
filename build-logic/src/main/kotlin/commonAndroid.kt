import com.android.build.gradle.BaseExtension
import extensions.PROJECT_JAVA_VERSION
import extensions.PROJECT_VERSION_CODE
import extensions.PROJECT_VERSION_NAME
import org.gradle.api.Project
import sources.ApkConfig


fun BaseExtension.commonAndroid(project: Project) {
    configureBuildConfig()
    configureCompileOptions(project)
    configureDefaultConfig(project)
}

private fun BaseExtension.configureBuildConfig() {
    buildFeatures.buildConfig = true
}

private fun BaseExtension.configureCompileOptions(project: Project) {
    compileOptions.sourceCompatibility = project.PROJECT_JAVA_VERSION
    compileOptions.targetCompatibility = project.PROJECT_JAVA_VERSION
}

private fun BaseExtension.configureDefaultConfig(project: Project) {
    compileSdkVersion(ApkConfig.COMPILE_SDK_VERSION)
    defaultConfig {
        minSdk = ApkConfig.MIN_SDK_VERSION
        targetSdk = ApkConfig.TARGET_SDK_VERSION
        versionCode = project.PROJECT_VERSION_CODE
        versionName = project.PROJECT_VERSION_NAME

        consumerProguardFiles(
            "consumer-rules.pro"
        )

        packagingOptions {
            resources.excludes += "META-INF/LICENSE-LGPL-2.1.txt"
            resources.excludes += "META-INF/LICENSE-LGPL-3.txt"
            resources.excludes += "META-INF/LICENSE-W3C-TEST"
            resources.excludes += "META-INF/DEPENDENCIES"
            resources.excludes += "*.proto"
        }

    }
}

