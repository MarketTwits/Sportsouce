

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.library") version "8.2.0" apply false
    id("org.jetbrains.kotlin.multiplatform") version "1.9.23" apply false
    id("org.jetbrains.compose") version "1.6.10-dev1575" apply false
}

tasks.withType<Wrapper>().configureEach {
    distributionType = Wrapper.DistributionType.BIN
    gradleVersion = libs.versions.gradle.get()
}
