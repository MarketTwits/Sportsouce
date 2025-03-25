package com.markettwits.selfupdater.components.selft_update.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.flipperdevices.selfupdater.api.SelfUpdaterApi
import com.flipperdevices.selfupdater.impl.api.SelfUpdaterApiImpl
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStoreFactory
import com.markettwits.selfupdater.thirdparty.api.selfUpdaterThirdPartyModule
import com.markettwits.selfupdater.thirdparty.github.di.thirdPartyGitHubModule
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationStorage
import com.markettwits.sportsouce.inappnotification.impl.storage.InAppNotificationStorageImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val selfUpdateComponentModule = module {
    includes(thirdPartyGitHubModule, selfUpdaterThirdPartyModule)
    singleOf(::InAppNotificationStorageImpl) bind InAppNotificationStorage::class
    singleOf(::SelfUpdaterApiImpl) bind SelfUpdaterApi::class
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::SelfUpdateStoreFactory)
}