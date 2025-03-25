package com.markettwits.sportsouce.settings.internal.settings_menu.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.intentActionModule
import com.markettwits.sportsouce.settings.api.api.settingsModule
import com.markettwits.sportsouce.settings.internal.settings_menu.store.SettingsStoreFactory
import com.markettwits.version.applicationVersionModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val settingsMenuModule = module {
    includes(settingsModule, intentActionModule, applicationVersionModule)
    singleOf(::SettingsStoreFactory)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
}