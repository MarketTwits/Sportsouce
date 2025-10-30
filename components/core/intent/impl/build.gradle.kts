plugins {
    alias(libs.plugins.kotlin.kmp.convention)
}
android {
    namespace = "com.markettwits.intent.impl"
}
kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.core)
        }
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(projects.components.core.intent.api)
        }
    }
}

