plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}


kotlin {

    android {
        namespace = "com.markettwits.selfupdater.thirdparty.github"
    }

    jvm()
    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.selfupdater.thirdparty.api)
            implementation(projects.components.selfupdater.api)
            implementation(libs.kotlinx.coroutines.core)
            implementation(projects.components.core.cloud)
            implementation(libs.ktor.core)
            implementation(libs.ktor.client.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(projects.components.core.buildConfig)
        }
        jsMain.dependencies {
            implementation(project.dependencies.enforcedPlatform(libs.jetbrains.kotlinWrappers.kotlinWrappersBom))
            implementation(libs.kotlin.browser)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation("io.ktor:ktor-client-mock:${libs.versions.ktor.get()}")
        }
    }
}
