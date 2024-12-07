enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
       // maven("https://jitpack.io")
        maven("https://artifactory-external.vkpartner.ru/artifactory/maven/")
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
       // maven("https://jitpack.io")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://artifactory-external.vkpartner.ru/artifactory/maven/")
    }
}

rootProject.name = "Sportsouce"
includeBuild("build-logic")
include(":app")
include(":desktop")
include(":starts:starts")
include(":start:start")
include(":cloud")
include(":root")
include(":auth:auth-service")
include(":auth:auth-flow")
include(":news")
include(":shop:catalog")
include(":shop:cloud")
include(":shop:item")
include(":shop:filter")
include(":shop:search")
include(":shop:cart")
include(":shop:order")
include(":shop:domain")
include(":teams-city")
include(":start:start-register")
include(":start:start-search")
include(":start:start-filter")
include(":start:start-support")
include(":profile:unauthorized")
include(":profile:authorized:profile")
include(":profile:authorized:members")
include(":profile:authorized:edit-profile")
include(":profile:authorized:change-password")
include(":profile:authorized:registrations")
include(":review:review")
include(":review:schedule")
include(":review:random")
include(":review:popular")
include(":starts:starts-common")
include(":bottombar")
include(":selfupdater:api")
include(":selfupdater:impl")
include(":selfupdater:unknown")
include(":selfupdater:ver")
include(":selfupdater:thirdparty:api")
include(":selfupdater:thirdparty:github")
include(":selfupdater:components")
include(":selfupdater:googleplay")
include(":club:cloud")
include(":club:dashboard")
include(":settings:api")
include(":settings:impl")
include(":inappnotification:api")
include(":inappnotification:impl")
include(":analytics:crashlytics")
include(":profile:root")
include(":intent:api")
include(":intent:impl")
include(":intent:composable")
include(":core:ui")
include(":core:time")
include(":core:theme")
include(":core:activityholder")
include("core:errors")
include(":core:log")
include(":core:cloud")
include(":core:cache")
include(":core:koin")
include(":selfupdater:googleplay")
include(":composable")
include(":shop:orders")
include(":start:start-cloud")