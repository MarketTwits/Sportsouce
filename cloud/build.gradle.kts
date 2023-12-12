@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.markettwits.cloud"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    packagingOptions {
        resources.excludes.add("META-INF/**")
    }
}

dependencies {
    api(libs.ktor.core)
    api(libs.ktor.client.json)
    api(libs.ktor.client.logging)
    api(libs.ktor.client.content.negotiation)
    api(libs.ktor.client.okhttp)
    api(libs.kotlinx.serialization.json)
}