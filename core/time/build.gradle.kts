plugins {
    alias(libs.plugins.kotlin.jvm.convention)
}

dependencies{
    implementation(libs.kotlinx.datetime)
    implementation(libs.multiplatform.locale)
}