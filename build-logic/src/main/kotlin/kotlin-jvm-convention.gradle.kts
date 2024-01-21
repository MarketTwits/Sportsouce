plugins {
    id("kotlin")
}
kotlin {
    jvmToolchain(localLibs.findVersion("jvm").get().toString().toInt())
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = localLibs.findVersion("jvm-dot").get().toString()
    }
}
