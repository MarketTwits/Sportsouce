import com.android.build.gradle.BaseExtension
import com.markettwits.sportsouce.extensions.PROJECT_VERSION_CODE
import com.markettwits.sportsouce.extensions.PROJECT_VERSION_NAME
import com.markettwits.sportsouce.sources.ApkConfig
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


fun BaseExtension.commonAndroid(project: Project) {
    configureBuildConfig()
    commonJava(project)
    configureDefaultConfig(project)
    suppressOptIn(project)
}

private fun BaseExtension.configureBuildConfig() {
    buildFeatures.buildConfig = true
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

@Suppress("MaxLineLength")
private fun suppressOptIn(project: Project) {
    project.tasks.withType<KotlinCompile>()
        .configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
                freeCompilerArgs.add("-Xexpect-actual-classes")
                optIn.addAll(
                    "com.google.accompanist.pager.ExperimentalPagerApi",
                    "androidx.compose.ui.ExperimentalComposeUiApi",
                    "androidx.compose.foundation.ExperimentalFoundationApi",
                    "kotlinx.serialization.ExperimentalSerializationApi",
                    "kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "com.squareup.anvil.annotations.ExperimentalAnvilApi",
                    "kotlin.time.ExperimentalTime",
                    "kotlin.RequiresOptIn",
                    "androidx.compose.animation.ExperimentalAnimationApi",
                    "com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi",
                    "androidx.compose.foundation.layout.ExperimentalLayoutApi"
                )
            }
        }
}