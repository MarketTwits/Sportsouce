plugins {
    alias(libs.plugins.kotlin.kmp.convention)
}
android{
    namespace = "com.markettwits.core.time"
}
kotlin {
    sourceSets {
        commonMain.dependencies{
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.datetime.ext)
        }
    }
}
