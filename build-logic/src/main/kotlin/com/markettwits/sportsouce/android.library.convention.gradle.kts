import com.android.build.api.dsl.LibraryExtension

plugins {
    id("com.android.library")
}

configure<LibraryExtension> {
    commonAndroid(project)
}
