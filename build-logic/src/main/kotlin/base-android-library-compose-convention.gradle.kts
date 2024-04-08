import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension

plugins {
    id("com.vk.vkompose")
}

configure<BaseExtension>{
    val extension = extensions.getByType<LibraryExtension>()
    extension.buildFeatures{
        compose = true
    }
    composeOptions{
        kotlinCompilerExtensionVersion  = localLibs.findVersion("compose").get().toString()
    }
}

//vkompose {
//    skippabilityCheck = false
//    // or
//
//    recompose {
//        isHighlighterEnabled = true
//        isLoggerEnabled = true
//    }
//
//    testTag {
//        isApplierEnabled = true
//        isDrawerEnabled = false
//        isCleanerEnabled = false
//    }
//
//    sourceInformationClean = true
//}