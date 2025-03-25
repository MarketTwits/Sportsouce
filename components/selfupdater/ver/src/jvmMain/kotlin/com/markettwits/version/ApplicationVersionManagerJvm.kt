package com.markettwits.version

import Sportsouce.components.selfupdater.ver.BuildConfig

class ApplicationVersionManagerJvm : ApplicationVersionManager {

    override fun currentDistribution(): ApplicationVersion =
        ApplicationVersion(
            versionName = BuildConfig.APP_VERSION,
            versionBuildNumber = BuildConfig.APP_VERSION_NUMBER.toString()
        )
}