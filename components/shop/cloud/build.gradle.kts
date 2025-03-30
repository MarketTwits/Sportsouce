plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kmp.convention)
}
android {
    namespace = "com.markettwits.shop.cloud"
}

kotlin{
    sourceSets{
        commonMain.dependencies {
            api(libs.ktor.client.json)
            implementation(libs.koin.core)
            implementation(libs.ktor.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.kotlinx.serialization.json)
            implementation(projects.components.core.koin)
            implementation(projects.components.core.cloud)
        }
    }
}
