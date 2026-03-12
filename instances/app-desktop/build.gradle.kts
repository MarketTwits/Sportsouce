import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose.hot-reload")
}

val desktopMainClass = "com.markettwits.sportsouce.app.desktop.MainKt"
val desktopPackageName = "SportSauce"
val desktopPackageVersion = libs.versions.versionName
val desktopVendor = "MarketTwits"
val desktopDescription = "SportSauce Desktop Application"
val desktopWindowsMenuGroup = "SportSauce"
val desktopWindowsUpgradeUuid = "9ec0a591-801b-4bf9-92ad-97d887e046dd"
val desktopMacBundleId = "com.markettwits.sportsouce.desktop"
fun env(name: String) = providers.environmentVariable(name)

tasks {
    withType<Jar> {
        manifest {
            attributes["Main-Class"] = desktopMainClass
        }
    }
}

configurations.configureEach {
    if (name.endsWith("jvmRuntimeClasspath")) {
        exclude(group = "org.jetbrains.compose.components", module = "components-ui-tooling-preview")
        exclude(group = "org.jetbrains.compose.ui", module = "ui-tooling-preview-desktop")
    }
}

kotlin {
    jvm()
    jvmToolchain(21)

    sourceSets.jvmMain.dependencies {
        implementation(compose.desktop.currentOs)
        implementation(projects.components.core.ui)
        implementation(libs.koin.core)
        implementation(projects.components.core.koin)
        implementation(projects.components.deeplink.api)
        implementation(projects.components.root)
        implementation(projects.components.core.theme)
        implementation(libs.bundles.decompose.compose)
        implementation(projects.components.core.cache)
        implementation(libs.kotlinx.coroutines.swing)
        implementation(libs.ktor.client.okhttp)
        implementation(libs.reaktive)
        implementation(libs.coroutines.interop)
    }
}

compose.desktop {
    application {

        mainClass = desktopMainClass
        jvmArgs(
            "-Dfile.encoding=UTF-8",
            "-Xms256m",
            "-Xmx1536m"
        )

        nativeDistributions {
            modules(
                "java.instrument",
                "java.management",
                "java.scripting",
                "jdk.unsupported"
            )

            targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.Msi, TargetFormat.Deb)

            packageName = desktopPackageName
            packageVersion = desktopPackageVersion.get()
            description = desktopDescription
            copyright = "© 2024 Sportsauce."
            vendor = desktopVendor

            linux {
                iconFile.set(project.file("desktopAppIcons/LinuxSportSauceIcon.png"))
            }
            windows {
                iconFile.set(project.file("desktopAppIcons/WindowsSportSauceIcon.ico"))
                shortcut = true
                menuGroup = desktopWindowsMenuGroup
                upgradeUuid = desktopWindowsUpgradeUuid
                perUserInstall = true
            }
            macOS {
                iconFile.set(project.file("desktopAppIcons/MacSportSauceIcon.icns"))
                packageName = desktopPackageName
                dockName = desktopPackageName
                bundleID = desktopMacBundleId

                signing {
                    sign.set(env("SPORTSAUCE_MAC_SIGN").map(String::toBoolean).orElse(false))
                    identity.set(env("SPORTSAUCE_MAC_SIGN_IDENTITY"))
                    keychain.set(env("SPORTSAUCE_MAC_KEYCHAIN"))
                }

                notarization {
                    appleID.set(env("SPORTSAUCE_MAC_NOTARY_APPLE_ID"))
                    password.set(env("SPORTSAUCE_MAC_NOTARY_PASSWORD"))
                    teamID.set(env("SPORTSAUCE_MAC_NOTARY_TEAM_ID"))
                }
            }
            appResourcesRootDir.set(project.layout.projectDirectory.dir("src/jvmMain/resources"))
        }

        buildTypes.release.proguard {
            configurationFiles.from(project.file("compose-desktop.pro"))
        }
    }
}
