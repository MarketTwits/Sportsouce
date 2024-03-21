enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "Sportsouce"
includeBuild("build-logic")
include(":app")
include(":starts")
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
include(":starts-common")
include(":cache")
