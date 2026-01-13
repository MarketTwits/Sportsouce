plugins {
    alias(libs.plugins.kotlin.kmp.convention)
}

kotlin {
    android {
        namespace = "com.markettwits.selfupdater.googleplay"
    }
}



dependencies {
    kotlin{
        sourceSets{
            androidMain.dependencies {
                implementation(projects.components.selfupdater.api)
                implementation(projects.components.inappnotification.api)
                implementation(projects.components.core.buildConfig)
                implementation(projects.components.core.activityholder)
                implementation("com.google.android.play:app-update:2.1.0")
                implementation("com.google.android.play:app-update-ktx:2.1.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")
            }
        }
    }
}
