plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)

}

android {
    namespace = "com.markettwits.profile"
}
dependencies {
    api(project(":cloud"))
    implementation(project(":auth"))
    implementation(project(":core-ui"))
    implementation(project(":change-password"))
    implementation(libs.bundles.decompose.compose)
    implementation (libs.material3.html.text)
    implementation(libs.kotlinx.datetime)
    // CALENDAR
    implementation ("com.maxkeppeler.sheets-compose-dialogs:calendar:1.2.0")

    implementation ("com.arkivanov.mvikotlin:mvikotlin:3.3.0")
    implementation ("com.arkivanov.mvikotlin:mvikotlin-main:3.3.0")
    implementation ("com.arkivanov.mvikotlin:mvikotlin-logging:3.3.0")
    implementation ("com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:3.3.0")
}