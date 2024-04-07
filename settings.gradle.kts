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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
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
include(":app")
include(":starts:starts")
include(":start:start")
include(":cloud")
include(":cloud-shop")
include(":cloud-clubs")
include(":core-cloud")
include(":core-ui")
include(":root")
include(":auth:auth-service")
include(":auth:auth-flow")
include(":news")
include(":shop:shop-catalog")
include(":teams-city")
include(":start:start-register")
include(":start:start-search")
include(":start:start-filter")
include(":start:start-support")
include(":profile:profile")
include(":profile:members")
include(":profile:edit-profile")
include(":profile:change-password")
include(":profile:registrations")
include(":review:review")
include(":review:schedule")
include(":review:random")
include(":review:popular")
include(":core-koin")
include(":starts:starts-common")
include(":cache")

include(":selfupdater:api")
include(":selfupdater:impl")
include(":selfupdater:unknown")
include(":selfupdater:thirdparty:api")
include(":selfupdater:thirdparty:github")
include(":selfupdater:components")

include(":settings:api")
include(":settings:impl")

include(":inappnotification:api")
include(":inappnotification:impl")

include(":analytics:api")
include(":analytics:crashlytics")
