plugins {
    alias(libs.plugins.kotlin.kmp.convention)
}
android {
    namespace = "org.markettwits.core.log"
}
kotlin {
    sourceSets {
        commonMain.dependencies {
           implementation(libs.kermit)
        }
    }
}