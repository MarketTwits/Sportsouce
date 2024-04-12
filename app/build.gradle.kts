plugins {
    id("android-application-convention")
    id("android-application-crashlytics-convention")
}

android {
    namespace = libs.versions.namespace.get()

    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.asProvider().get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {

        defaultConfig {
            resourceConfigurations.addAll(setOf("en", "ru"))
        }
        debug {
            isMinifyEnabled = true
            isShrinkResources = true
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
    implementation(projects.core.theme)
    implementation(projects.coreUi)
    implementation(projects.root)
    implementation(projects.cache)
    implementation(projects.analytics.crashlytics)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.tracer.crash.report)
    implementation(libs.koin.android)
}


