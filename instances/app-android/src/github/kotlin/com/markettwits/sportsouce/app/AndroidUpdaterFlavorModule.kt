package com.markettwits.sportsouce.app

import com.markettwits.selfupdater.thirdparty.api.AndroidSideloadUpdaterFactory
import com.markettwits.selfupdater.thirdparty.api.SelfUpdaterThirdParty
import org.koin.dsl.module

internal val androidUpdaterFlavorModule = module {
    single<AndroidSideloadUpdaterFactory> {
        AndroidSideloadUpdaterFactory { context, parser, notifications ->
            SelfUpdaterThirdParty(context, parser, notifications)
        }
    }
}
