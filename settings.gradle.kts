enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
        maven("https://artifactory-external.vkpartner.ru/artifactory/maven/")
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://artifactory-external.vkpartner.ru/artifactory/maven/")
    }
}

rootProject.name = "Sportsouce"
includeBuild("build-logic")

// Launch
include(":app")
include(":browser")
include(":desktop")
// Auth
include(":auth:auth-service")
include(":auth:auth-flow")
include(":auth:auth-cloud")
// Profile
include(":profile:root")
include(":profile:cloud")
include(":profile:unauthorized")
include(":profile:authorized:profile")
include(":profile:authorized:members")
include(":profile:authorized:edit-profile")
include(":profile:authorized:change-password")
include(":profile:authorized:registrations")
// News
include(":news:news")
include(":news:news-cloud")
// Shop
include(":shop:catalog")
include(":shop:cloud")
include(":shop:item")
include(":shop:filter")
include(":shop:search")
include(":shop:cart")
include(":shop:order")
include(":shop:domain")
include(":shop:orders")
// Start
include(":start:start")
include(":start:start-register")
include(":start:start-search")
include(":start:start-filter")
include(":start:start-support")
include(":start:start-cloud")
// Starts
include(":starts:starts")
include(":starts:starts-random")
include(":starts:starts-popular")
include(":starts:starts-common")
include(":starts:starts-cloud")
// Club
include(":club:cloud")
include(":club:dashboard")
// Teams and Cities
include(":teams-city")
// Selfupdater
include(":selfupdater:api")
include(":selfupdater:impl")
include(":selfupdater:unknown")
include(":selfupdater:ver")
include(":selfupdater:thirdparty:api")
include(":selfupdater:thirdparty:github")
include(":selfupdater:components")
include(":selfupdater:googleplay")
// Settings
include(":settings:api")
include(":settings:impl")
// inappnotification
include(":inappnotification:api")
include(":inappnotification:impl")
// Analitics
include(":analytics:crashlytics")
// Intent
include(":intent:api")
include(":intent:impl")
include(":intent:composable")
// Core
include(":core:ui")
include(":core:time")
include(":core:theme")
include(":core:activityholder")
include(":core:errors")
include(":core:log")
include(":core:cloud")
include(":core:cache")
include(":core:koin")
include(":core:paging")
include(":core:decompose")
// Extra
include(":review")
include(":bottombar")
include(":root")
