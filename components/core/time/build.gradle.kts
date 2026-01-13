plugins {
    alias(libs.plugins.kotlin.kmp.convention)
}
kotlin {

    android {
        namespace = "com.markettwits.core.time"
    }

    sourceSets {
        commonMain.dependencies{
            api(libs.kotlinx.datetime)
            // api(libs.kotlinx.datetime.ext)
        }
        commonTest.dependencies {
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.kotlin.test)
        }
    }
}
