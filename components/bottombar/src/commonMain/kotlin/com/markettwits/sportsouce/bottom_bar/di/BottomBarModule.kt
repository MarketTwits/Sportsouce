package com.markettwits.sportsouce.bottom_bar.di

import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarVisibilityListener
import com.markettwits.sportsouce.bottom_bar.component.storage.BottomBarStorage
import com.markettwits.sportsouce.bottom_bar.component.storage.BottomBarStorageImpl
import com.markettwits.sportsouce.settings.api.api.settingsModule
import org.koin.dsl.module

val bottomBarModule = module {
    includes(settingsModule)
    single<BottomBarStorage> { BottomBarStorageImpl }
    single<BottomBarVisibilityListener> { get<BottomBarStorage>() }
}