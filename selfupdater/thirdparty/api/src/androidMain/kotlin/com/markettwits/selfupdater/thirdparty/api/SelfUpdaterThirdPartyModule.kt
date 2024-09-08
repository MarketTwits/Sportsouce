package com.markettwits.selfupdater.thirdparty.api

import android.content.Context
import com.flipperdevices.selfupdater.api.SelfUpdaterSourceApi
import com.flipperdevices.selfupdater.googleplay.api.SelfUpdaterGooglePlay
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent

actual val selfUpdaterThirdPartyModule = module {

    val context: Context by KoinJavaComponent.inject(Context::class.java)

//    SelfUpdaterGooglePlay(
//        context = context,
//        inAppNotificationStorage = get()
//    )

    single<SelfUpdaterSourceApi> {
        SelfUpdaterGooglePlay(
            context = context,
            inAppNotificationStorage = get()
        )
//        SelfUpdaterThirdParty(
//            context = context,
//            updateParser = get(),
//            inAppNotificationStorage = get()
//        )
    }
}