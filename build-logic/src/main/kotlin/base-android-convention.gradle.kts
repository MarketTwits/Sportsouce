import com.android.build.gradle.BaseExtension

configure<BaseExtension> {

    compileSdkVersion(localLibs.findVersion("compileSdk").get().toString().toInt())
    defaultConfig {
        minSdk = localLibs.findVersion("minSdk").get().toString().toInt()
        targetSdk = localLibs.findVersion("targetSdk").get().toString().toInt()
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

