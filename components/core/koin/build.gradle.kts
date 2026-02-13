plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {

    android {
        namespace = "com.markettwits.core.koin"
    }

    sourceSets{
        commonMain.dependencies {
            implementation(libs.decompose)
            implementation(libs.mvikotlin)
            implementation(libs.koin.core)
        }
    }
}
