package com.markettwits.selfupdater.thirdparty.api

import com.markettwits.selfupdater.api.SelfUpdaterSourceApi
import org.koin.core.module.Module
import org.koin.dsl.module

actual val selfUpdaterThirdPartyModule: Module = module {

    single<SelfUpdaterSourceApi> {
        SelfUpdaterDesktop(parser = get(), notifications = get())
    }
}
