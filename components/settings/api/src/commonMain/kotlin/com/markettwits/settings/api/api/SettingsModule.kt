package com.markettwits.settings.api.api

import com.markettwits.cahce.ObservableCache
import com.markettwits.settings.api.api.params.ApplicationSettings
import com.markettwits.settings.api.internal.ApplicationSettingsCache
import com.markettwits.settings.api.internal.SettingsRepositoryBase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val settingsModule = module {
    singleOf(::SettingsRepositoryBase) bind MutableSettingsRepository::class
    single<SettingsRepository> { get<MutableSettingsRepository>() }
    single<ObservableCache<ApplicationSettings>> { ApplicationSettingsCache }
}