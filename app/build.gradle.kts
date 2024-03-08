plugins {
    id("android-application-convention")
}

android {
    namespace = libs.versions.namespace.get()

    buildFeatures {
        compose = true
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
//        debug {
//            isMinifyEnabled = true
//            isShrinkResources = true
//            isDebuggable = true
//            proguardFiles(
//                getDefaultProguardFile("proguard-android.txt"),
//                "proguard-rules.pro"
//            )
//        }
    }
}

dependencies {
    implementation(projects.coreUi)
    implementation(projects.root)
    implementation(projects.cache)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.koin.android)
}