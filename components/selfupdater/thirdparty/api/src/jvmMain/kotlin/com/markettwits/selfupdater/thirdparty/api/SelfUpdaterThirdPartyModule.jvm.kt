package com.markettwits.selfupdater.thirdparty.api

import com.markettwits.selfupdater.api.SelfUpdaterSourceApi
import com.markettwits.selfupdater.unknown.api.SelfUpdaterUnknown
import org.koin.core.module.Module
import org.koin.dsl.module

actual val selfUpdaterThirdPartyModule: Module = module {

    single<SelfUpdaterSourceApi> {
        SelfUpdaterUnknown()
    }
}