plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
}


kotlin {

    android {
        namespace = "com.markettwits.shop.domain"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.shop.cloud)
        }
    }
}
