plugins {
    alias(libs.plugins.kotlin.kmp.convention)
}
kotlin {

    android {
        namespace = "org.markettwits.core.log"
    }

    sourceSets {
        commonMain.dependencies {
           implementation(libs.kermit)
        }
    }
}
