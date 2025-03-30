package com.markettwits.version

import com.markettwits.buildkonfig.BuildKonfig

class ApplicationVersionManagerJvm : ApplicationVersionManager {

    override fun currentDistribution(): ApplicationVersion =
        ApplicationVersion(
            versionName = BuildKonfig.APP_VERSION,
            versionBuildNumber = BuildKonfig.APP_VERSION_NUMBER.toString()
        )
}