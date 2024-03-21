plugins {
    alias(libs.plugins.android.library.compose.convention)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "org.markettwits.shop_catalog"
}

dependencies {
    implementation(projects.cloudShop)
}