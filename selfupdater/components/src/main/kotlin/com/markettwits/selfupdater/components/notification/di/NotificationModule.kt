package com.markettwits.selfupdater.components.notification.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.flipperdevices.inappnotification.impl.api.InAppNotificationRendererImpl
import com.markettwits.inappnotification.api.InAppNotificationRenderer
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStoreFactory
import com.markettwits.selfupdater.components.selft_update.di.selfUpdateComponentModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val notificationModule = module {

    includes(selfUpdateComponentModule)

    //singleOf(::InAppNotificationStorageImpl) bind InAppNotificationStorage::class
    singleOf(::InAppNotificationRendererImpl) bind InAppNotificationRenderer::class
    // singleOf(::SelfUpdaterApiImpl) bind SelfUpdaterApi::class
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::InAppNotificationStoreFactory)
    // singleOf(::SelfUpdateStoreFactory)
}