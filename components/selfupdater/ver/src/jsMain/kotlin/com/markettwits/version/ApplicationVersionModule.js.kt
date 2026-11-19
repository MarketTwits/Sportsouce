package com.markettwits.version

import com.markettwits.buildkonfig.BuildKonfig
import org.koin.core.module.Module
import org.koin.dsl.module

actual val applicationVersionModule: Module = module {
    single<ApplicationVersionManager> {
        object : ApplicationVersionManager {
            override fun currentDistribution(): ApplicationVersion = ApplicationVersion(
                versionName = BuildKonfig.APP_VERSION,
                versionBuildNumber = BuildKonfig.APP_VERSION_NUMBER.toString(),
            )
        }
    }
}
