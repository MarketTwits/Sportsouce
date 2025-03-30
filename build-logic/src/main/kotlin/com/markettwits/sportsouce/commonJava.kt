import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun BaseExtension.commonJava(project: Project) {

    project.tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    project.extensions.findByType<JavaPluginExtension>()?.run {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    project.extensions.findByType<BaseExtension>()?.run {
        compileOptions.sourceCompatibility = JavaVersion.VERSION_17
        compileOptions.targetCompatibility = JavaVersion.VERSION_17
    }

    project.extensions.findByType<KotlinMultiplatformExtension>()?.run {
        targets.filterIsInstance<HasConfigurableKotlinCompilerOptions<*>>()
            .mapNotNull { it.compilerOptions as? KotlinJvmCompilerOptions }
            .onEach { androidTarget ->
                androidTarget.jvmTarget.set(JvmTarget.JVM_17)
            }
    }

    project.tasks
        .withType<KotlinCompile>()
        .configureEach {
            compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
        }
}