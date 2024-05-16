plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    id("ru.ok.tracer") version ("0.2.15")
}

android.namespace = "com.markettwits.analitics.crashlytics"
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(libs.tracer.crash.report)
        }
    }
}
tracer {
    create("defaultConfig") {
        pluginToken = "JlDv2pDQlHq4qbZPeKTv66E5zF8Zpvtz39FzTpEmdgY1"
        appToken = "ylXqrrPexa5weKlB4sAHYteLVnvqI5SQYx4Aghh6GKr"
    }
}