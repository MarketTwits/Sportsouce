plugins {
    //kotlin("jvm")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}
kotlin {
    jvm()
    sourceSets.jvmMain.dependencies {
        implementation(compose.desktop.currentOs)
        // implementation(compose.components.resources)
        //implementation(projects.core.ui)
        implementation(compose.desktop.common)
        implementation(projects.root)
        implementation(projects.core.theme)
        implementation(libs.bundles.decompose.compose)
        implementation(projects.cache)
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.4")
        implementation("com.badoo.reaktive:reaktive:1.2.3")
        implementation("com.badoo.reaktive:coroutines-interop:1.2.3")
        // testImplementation(compose.desktop.uiTestJUnit4)
    }
}
//dependencies {
//    // Note, if you develop a library, you should use compose.desktop.common.
//    // compose.desktop.currentOs should be used in launcher-sourceSet
//    // (in a separate module for demo project and in testMain).
//    // With compose.desktop.common you will also lose @Preview functionality
//    implementation(compose.desktop.currentOs)
//   // implementation(compose.components.resources)
//    implementation(projects.core.ui)
//    implementation(projects.root)
//    implementation(projects.core.theme)
//    implementation(libs.bundles.decompose.compose)
//    implementation(projects.cache)
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.4")
//    implementation("com.badoo.reaktive:reaktive:1.2.3")
//    implementation("com.badoo.reaktive:coroutines-interop:1.2.3")
//    testImplementation(compose.desktop.uiTestJUnit4)
//}
compose.desktop {
    application {
        mainClass = "com.markettwits.sportsouce.app.MainKt"
    }
}
//compose.desktop {
//    application {
//        mainClass = "MainKt"
//
//        nativeDistributions {
//            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
//            packageName = "KotlinJvmComposeDesktopApplication"
//            packageVersion = "1.0.0"
//        }
//    }
//}

