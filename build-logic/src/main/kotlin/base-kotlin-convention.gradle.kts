import org.gradle.kotlin.dsl.withType

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = localLibs.findVersion("jvm-dot").get().toString()
    }
}