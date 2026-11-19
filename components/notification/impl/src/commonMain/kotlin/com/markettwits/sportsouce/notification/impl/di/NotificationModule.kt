package com.markettwits.sportsouce.notification.impl.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.core_cloud.provider.SportSauceHttpClientProvider
import com.markettwits.sportsouce.auth.service.api.authDataSourceModule
import com.markettwits.sportsouce.notification.api.NotificationApi
import com.markettwits.sportsouce.notification.impl.api.NotificationApiImpl
import com.markettwits.sportsouce.notification.impl.data.*
import com.markettwits.sportsouce.notification.impl.domain.NotificationRepository
import com.markettwits.sportsouce.notification.impl.presentation.store.NotificationStoreFactory
import org.koin.dsl.module

/** Installed once at the application root; every tab receives the same notification service. */
val userNotificationModule = module {
    includes(authDataSourceModule)
    single { NotificationRemoteApi(SportSauceHttpClientProvider()) }
    single<NotificationRepository> { NotificationRepositoryBase(get(), get()) }
    factory { NotificationStoreFactory(DefaultStoreFactory(), get()) }
    single<NotificationApi> { NotificationApiImpl(get(), get()) }
}
