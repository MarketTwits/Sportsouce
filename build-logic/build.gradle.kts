plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(libs.android.gradle)
    implementation(libs.kotlin.gradle)
}
