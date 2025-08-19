package com.markettwits.selfupdater.thirdparty.api

import android.content.Context
import com.markettwits.selfupdater.api.SelfUpdaterSourceApi
import com.markettwits.selfupdater.googleplay.api.SelfUpdaterGooglePlay
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent

actual val selfUpdaterThirdPartyModule = module {

    val context: Context by KoinJavaComponent.inject(Context::class.java)

    single<SelfUpdaterSourceApi> {
        SelfUpdaterGooglePlay(
            context = context,
            inAppNotificationStorage = get()
        )
    }
}