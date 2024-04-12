package com.markettwits.settings.internal.settings_menu.di

import android.content.Context
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.IntentAction
import com.markettwits.IntentActionBase
import com.markettwits.settings.api.api.settingsModule
import com.markettwits.settings.internal.settings_menu.store.SettingsStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

val settingsMenuModule = module {
    includes(settingsModule)
    val context: Context by inject(Context::class.java)
    single<Context> { context }
    singleOf(::SettingsStoreFactory)
    singleOf(::IntentActionBase) bind IntentAction::class
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
}