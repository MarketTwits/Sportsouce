package com.markettwits.sportsouce.settings.internal.change_theme.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.settings.api.api.settingsModule
import com.markettwits.sportsouce.settings.internal.change_theme.store.ChangeThemeStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val changeThemeModule = module {
    includes(settingsModule)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::ChangeThemeStoreFactory)
}