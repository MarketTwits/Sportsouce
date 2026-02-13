plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.tracer.android)
}

kotlin {

    android {
        namespace = "com.markettwits.analitics.crashlytics"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.components.core.log)
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(projects.components.core.buildConfig)
            implementation(libs.tracer.crash.report.native)
            implementation(libs.tracer.crash.report)
            implementation(libs.tracer.disk.usage)
            implementation(libs.tracer.heap.dump)
            implementation(libs.tracer.profiler.sampling)
        }
    }
}
