package com.markettwits.settings.internal.change_theme.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.settings.api.api.settingsModule
import com.markettwits.settings.internal.change_theme.store.ChangeThemeStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val changeThemeModule = module {
    includes(settingsModule)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::ChangeThemeStoreFactory)
}