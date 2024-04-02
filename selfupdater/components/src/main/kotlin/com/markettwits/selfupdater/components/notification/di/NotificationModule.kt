package com.markettwits.selfupdater.components.notification.di

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.inappnotification.api.InAppNotificationRenderer
import com.markettwits.inappnotification.api.InAppNotificationStorage
import com.flipperdevices.inappnotification.impl.api.InAppNotificationRendererImpl
import com.flipperdevices.inappnotification.impl.storage.InAppNotificationStorageImpl
import com.flipperdevices.selfupdater.api.SelfUpdaterApi
import com.flipperdevices.selfupdater.impl.api.SelfUpdaterApiImpl
import com.markettwits.selfupdater.thirdparty.api.SelfUpdaterThirdParty
import com.markettwits.selfupdater.thirdparty.github.di.thirdPartyGitHubModule
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStoreFactory
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStoreFactory
import org.koin.dsl.module

fun notificationModule(context: Context) = module(createdAtStart = true) {
    includes(thirdPartyGitHubModule)
    single<InAppNotificationStorage> {
        InAppNotificationStorageImpl()
    }
    single<InAppNotificationRenderer> {
        InAppNotificationRendererImpl()
    }
    single<SelfUpdaterApi> {
        SelfUpdaterApiImpl(
            SelfUpdaterThirdParty(
                context = context,
                version = context.getPackageInfo().versionName,
                updateParser = get(),
                inAppNotificationStorage = get(),
            )
        )
    }
    single<StoreFactory> {
        DefaultStoreFactory()
    }
    single<InAppNotificationStoreFactory> {
        InAppNotificationStoreFactory(get(), get())
    }

    //SelfUpdate
    single<SelfUpdateStoreFactory> {
        SelfUpdateStoreFactory(
            get(),
            get()
        )
    }
}

fun Context.getPackageInfo(): PackageInfo {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
    } else {
        packageManager.getPackageInfo(packageName, 0)
    }
}