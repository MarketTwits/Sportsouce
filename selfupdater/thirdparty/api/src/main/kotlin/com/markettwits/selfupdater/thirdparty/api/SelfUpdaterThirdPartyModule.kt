package com.markettwits.selfupdater.thirdparty.api

import android.content.Context
import com.flipperdevices.selfupdater.api.SelfUpdaterSourceApi
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent

val selfUpdaterThirdPartyModule = module {
    val context: Context by KoinJavaComponent.inject(Context::class.java)

    single<SelfUpdaterSourceApi> {
        SelfUpdaterThirdParty(
            context = context,
            updateParser = get(),
            inAppNotificationStorage = get()
        )
    }
}