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

include(
    ":instances:app-android",
    ":instances:app-browser",
    ":instances:app-desktop",

    ":components:auth:auth-service",
    ":components:auth:auth-flow",
    ":components:auth:auth-cloud",
    ":components:profile:root",
    ":components:profile:cloud",
    ":components:profile:unauthorized",
    ":components:profile:authorized:profile",
    ":components:profile:authorized:members",
    ":components:profile:authorized:edit-profile",
    ":components:profile:authorized:change-password",
    ":components:profile:authorized:registrations",
    ":components:news:news",
    ":components:news:news-cloud",
    ":components:news:news-common",
    ":components:shop:catalog",
    ":components:shop:cloud",
    ":components:shop:item",
    ":components:shop:filter",
    ":components:shop:search",
    ":components:shop:cart",
    ":components:shop:order",
    ":components:shop:domain",
    ":components:shop:orders",
    ":components:start:start",
    ":components:start:start-register",
    ":components:start:start-search",
    ":components:start:start-filter",
    ":components:start:start-support",
    ":components:start:start-cloud",
    ":components:starts:starts",
    ":components:starts:starts-random",
    ":components:starts:starts-popular",
    ":components:starts:starts-common",
    ":components:starts:starts-cloud",
    ":components:club:cloud",
    ":components:club:dashboard",
    ":components:teams-city",
    ":components:selfupdater:api",
    ":components:selfupdater:impl",
    ":components:selfupdater:unknown",
    ":components:selfupdater:ver",
    ":components:selfupdater:thirdparty:api",
    ":components:selfupdater:thirdparty:github",
    ":components:selfupdater:components",
    ":components:selfupdater:googleplay",
    ":components:settings:api",
    ":components:settings:impl",
    ":components:inappnotification:api",
    ":components:inappnotification:impl",
    ":components:analytics:crashlytics",
    ":components:intent:api",
    ":components:intent:impl",
    ":components:intent:composable",
    ":components:core:ui",
    ":components:core:time",
    ":components:core:theme",
    ":components:core:activityholder",
    ":components:core:errors",
    ":components:core:log",
    ":components:core:cloud",
    ":components:core:cache",
    ":components:core:koin",
    ":components:core:paging",
    ":components:core:decompose",
    ":components:review",
    ":components:bottombar",
    ":components:root"
)
