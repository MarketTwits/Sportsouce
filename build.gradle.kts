plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.kmp.compose) apply false
    alias(libs.plugins.kotlin.kmp) apply false
    alias(libs.plugins.kotlin.android) apply false
}

tasks.withType<Wrapper>().configureEach {
    distributionType = Wrapper.DistributionType.BIN
    gradleVersion = libs.versions.gradle.get()
}