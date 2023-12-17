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
    implementation(project(":starts"))
    implementation(project(":root"))
    implementation(project(":core-ui"))
    implementation(libs.decompose.android)
    implementation(libs.decompose)
    implementation(libs.decompose.compose.extension)

}