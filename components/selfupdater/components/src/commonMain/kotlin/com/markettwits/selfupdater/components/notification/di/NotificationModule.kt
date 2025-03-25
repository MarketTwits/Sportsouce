package com.markettwits.selfupdater.components.notification.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStoreFactory
import com.markettwits.selfupdater.components.selft_update.di.selfUpdateComponentModule
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationRenderer
import com.markettwits.sportsouce.inappnotification.impl.api.InAppNotificationRendererImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val notificationModule = module {
    includes(selfUpdateComponentModule)
    singleOf(::InAppNotificationRendererImpl) bind InAppNotificationRenderer::class
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::InAppNotificationStoreFactory)
}