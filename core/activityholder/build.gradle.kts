plugins {
    id("android-library-convention")
}

kotlin {
    jvmToolchain(localLibs.findVersion("jvm-dot").get().toString().toInt())
}
android {
    namespace = "com.markettwits.activityholder"
}

dependencies {
    implementation(libs.appcompat)
}
