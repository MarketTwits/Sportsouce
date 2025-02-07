package com.markettwits.selfupdater.thirdparty.api

import com.flipperdevices.selfupdater.api.SelfUpdaterSourceApi
import com.flipperdevices.selfupdater.api.SelfUpdaterUnknown
import org.koin.core.module.Module
import org.koin.dsl.module

actual val selfUpdaterThirdPartyModule: Module = module {
    single<SelfUpdaterSourceApi> {
       SelfUpdaterUnknown()
    }
}