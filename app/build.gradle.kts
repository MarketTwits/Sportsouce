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
}

dependencies {
    implementation(project(":root"))
    implementation(libs.bundles.composeUiBundle)
    implementation(libs.bundles.decompose.compose)
    implementation(libs.decompose.android)
    implementation(libs.koin.android)
    implementation(project(":list"))
    implementation(project(":detail"))
}