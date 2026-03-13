import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.android.build.api.dsl.LibraryExtension
import com.markettwits.sportsouce.extensions.PROJECT_VERSION_CODE
import com.markettwits.sportsouce.extensions.PROJECT_VERSION_NAME
import com.markettwits.sportsouce.sources.ApkConfig
import org.gradle.api.Project


fun ApplicationExtension.commonAndroid(project: Project) {
    configureBuildConfig()
    commonJava(project)
    configureDefaultConfig(project)
}

fun LibraryExtension.commonAndroid(project: Project) {
    configureBuildConfig()
    commonJava(project)
    configureDefaultConfig(project)
}

fun KotlinMultiplatformAndroidLibraryTarget.commonAndroid() {
    compileSdk = ApkConfig.COMPILE_SDK_VERSION
    minSdk = ApkConfig.MIN_SDK_VERSION

    packaging.resources.excludes.addAll(commonPackagingExcludes)
}

private val commonPackagingExcludes = listOf(
    "META-INF/LICENSE-LGPL-2.1.txt",
    "META-INF/LICENSE-LGPL-3.txt",
    "META-INF/LICENSE-W3C-TEST",
    "META-INF/DEPENDENCIES",
    "*.proto"
)

private fun ApplicationExtension.configureBuildConfig() {
    buildFeatures.buildConfig = true
    buildFeatures.resValues = true
}

private fun LibraryExtension.configureBuildConfig() {
    buildFeatures.buildConfig = true
}

private fun ApplicationExtension.configureDefaultConfig(project: Project) {
    compileSdk = ApkConfig.COMPILE_SDK_VERSION
    defaultConfig {
        minSdk = ApkConfig.MIN_SDK_VERSION
        targetSdk = ApkConfig.TARGET_SDK_VERSION
        versionCode = project.PROJECT_VERSION_CODE
        versionName = project.PROJECT_VERSION_NAME
    }

    packaging {
        resources.excludes.addAll(commonPackagingExcludes)
    }
}

private fun LibraryExtension.configureDefaultConfig(project: Project) {
    compileSdk = ApkConfig.COMPILE_SDK_VERSION
    defaultConfig {
        minSdk = ApkConfig.MIN_SDK_VERSION
        val consumerRules = project.file("consumer-rules.pro")
        if (consumerRules.exists()) {
            consumerProguardFiles(consumerRules)
        }
    }

    packaging {
        resources.excludes.addAll(commonPackagingExcludes)
    }
}
