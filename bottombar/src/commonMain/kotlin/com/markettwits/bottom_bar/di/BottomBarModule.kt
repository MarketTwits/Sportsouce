package com.markettwits.bottom_bar.di

import com.markettwits.bottom_bar.component.listener.BottomBarVisibilityListener
import com.markettwits.bottom_bar.component.storage.BottomBarStorage
import com.markettwits.bottom_bar.component.storage.BottomBarStorageImpl
import com.markettwits.settings.api.api.settingsModule
import org.koin.dsl.module

val bottomBarModule = module {
    includes(settingsModule)
    single<BottomBarStorageImpl> { BottomBarStorageImpl(get()) }
    single<BottomBarVisibilityListener> { get<BottomBarStorageImpl>() }
    single<BottomBarStorage> { get<BottomBarStorageImpl>() }
}