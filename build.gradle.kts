import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.RootPackageJsonTask

plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.kmp.compose) apply false
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.kotlin.kmp) apply false
}

tasks.withType<Wrapper>().configureEach {
    distributionType = Wrapper.DistributionType.BIN
    gradleVersion = libs.versions.gradle.get()
}

// Kotlin/JS npm tasks still resolve configurations while the configuration cache is being written,
// which triggers “Resolution of the configuration ... was attempted without an exclusive lock”
// when the cache is enabled. Opt out of the cache for these tasks to keep JS runs stable.
tasks.withType<KotlinNpmInstallTask>().configureEach {
    notCompatibleWithConfigurationCache("Kotlin/JS npm resolution is not configuration-cache safe yet")
}
tasks.withType<RootPackageJsonTask>().configureEach {
    notCompatibleWithConfigurationCache("Kotlin/JS npm resolution is not configuration-cache safe yet")
}
