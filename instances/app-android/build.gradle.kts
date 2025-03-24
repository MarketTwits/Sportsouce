import sources.ApkConfig

plugins {
    id("android.application.convention")
    id("android.application.crashlytics.convention")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = ApkConfig.APPLICATION_ID

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        defaultConfig {
            resourceConfigurations.addAll(setOf("en", "ru"))
        }

        kotlinOptions {
            jvmTarget = "11"
        }

        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
dependencies {
    implementation(projects.components.core.theme)
    implementation(projects.components.core.ui)
    implementation(projects.components.root)
    implementation(projects.components.core.cache)
    implementation(projects.components.analytics.crashlytics)
    implementation(projects.components.core.activityholder)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.compose.activity)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.tracer.crash.report)
    implementation(libs.koin.android)
    implementation(projects.components.core.koin)
}
