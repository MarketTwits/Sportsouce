plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}
kotlin {
    js { browser { testTask { useKarma { useChromeHeadless() } } } }

    android {
        namespace = "com.markettwits.selfupdater.thirdparty.api"
        androidResources { enable = true }
        withHostTestBuilder {}.configure { isIncludeAndroidResources = true }
        withDeviceTestBuilder {}.configure { instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(projects.components.selfupdater.googleplay)
            implementation(projects.components.core.activityholder)
            implementation(libs.androidx.core)
        }
        commonMain.dependencies {
            implementation(projects.components.selfupdater.api)
            implementation(projects.components.inappnotification.api)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.koin.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        getByName("androidHostTest").dependencies {
            implementation("junit:junit:4.13.2")
            implementation("org.robolectric:robolectric:4.17")
            implementation("androidx.test:core:1.7.0")
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        getByName("androidDeviceTest").dependencies {
            implementation("androidx.test:runner:1.6.2")
            implementation("androidx.test.ext:junit:1.2.1")
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.core)
        }
        jvmMain.dependencies {
            implementation(projects.components.core.buildConfig)
        }
        jsMain.dependencies {
            implementation(projects.components.core.buildConfig)
            implementation(libs.kotlinx.serialization.json)
            implementation(project.dependencies.enforcedPlatform(libs.jetbrains.kotlinWrappers.kotlinWrappersBom))
            implementation(libs.kotlin.browser)
        }
    }
}
