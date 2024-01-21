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
    }
}


rootProject.name = "Sportsouce"
includeBuild("build-logic")
include(":app")
include(":starts")
include(":start")
include(":cloud")
include(":core-ui")
include(":root")
include(":auth")
include(":news")
include(":start-register")
include(":profile:profile")
include(":profile:edit-profile")
include(":profile:change-password")
include(":profile:registrations")
include(":review:review")
include(":review:start-filter")
include(":review:schedule")
include(":review:random")
include(":review:popular")
include(":core-koin")