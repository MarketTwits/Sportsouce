plugins {
    alias(libs.plugins.kotlin.kmp.convention)
    alias(libs.plugins.kotlin.kmp.compose.convention)
}

// The architecture test protects every production source set, including platform entry points.
// Track these inputs so a change in another module cannot leave this test incorrectly up to date.
tasks.withType<Test>().matching { it.name == "jvmTest" }.configureEach {
    systemProperty("sportsauce.repositoryRoot", rootProject.projectDir.absolutePath)
    inputs.files(
        rootProject.fileTree("components") {
            include("**/src/*Main/**/*.kt", "**/src/main/**/*.kt")
            exclude("**/build/**")
        },
        rootProject.fileTree("instances") {
            include("**/src/*Main/**/*.kt", "**/src/main/**/*.kt")
            exclude("**/build/**")
        },
    ).withPropertyName("navigationArchitectureSources").withPathSensitivity(PathSensitivity.RELATIVE)
}

kotlin {

    android {
        namespace = "org.markettwits.core.core.ui"
    }

    sourceSets {

        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            api(libs.ktor.client.okhttp)
            implementation(libs.compose.activity)
        }

        commonMain.dependencies {
            api(libs.compose.runtime)
            api(libs.compose.foundation)
            api(libs.compose.material3)
            api(libs.compose.material.icons.extended)
            api(libs.compose.components.ui.tooling.preview)
            api(libs.compose.ui)
            api(libs.haze)
            api(libs.coil.compose)
            api(libs.coil.network)
            api(libs.coil.mp)
            api(libs.richeditor)
            api(libs.composeMaterial3WindowSize)
            api(libs.composeMaterial3Adaptive)
            implementation(libs.compose.components.resources)
            implementation(libs.kotlinx.serialization.json)
            implementation(projects.components.core.time)
        }
        
        jvmMain.dependencies {
            api(libs.compose.animation)
            api(libs.ktor.client.okhttp)
        }

        jsMain.dependencies {
            api(libs.ktor.client.js)
        }
    }
    sourceSets.commonTest.dependencies {
        implementation(libs.kotlin.test)
    }

}
