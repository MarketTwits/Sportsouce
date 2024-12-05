plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    id("ru.ok.tracer") version ("0.2.15")
}

android.namespace = "com.markettwits.analitics.crashlytics"
kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.log)
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(libs.tracer.crash.report)
            implementation(libs.tracer.disk.usage)
            implementation(libs.tracer.heap.dump)
        }
    }
}