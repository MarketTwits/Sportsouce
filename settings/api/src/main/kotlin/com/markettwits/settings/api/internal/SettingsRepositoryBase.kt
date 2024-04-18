package com.markettwits.settings.api.internal


import com.markettwits.cahce.ObservableCache
import com.markettwits.settings.api.api.MutableSettingsRepository
import com.markettwits.settings.api.api.params.ApplicationSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class SettingsRepositoryBase(
    private val settingsCache: ObservableCache<ApplicationSettings>
) : MutableSettingsRepository {
    override suspend fun updateSettings(settings: ApplicationSettings) {
        settingsCache.set(value = settings)
    }

    override fun observeSettings(): Flow<ApplicationSettings> =
        settingsCache.observe().map {
            it ?: ApplicationSettings()
        }

    override suspend fun fetchSettings(): ApplicationSettings =
        settingsCache.get() ?: ApplicationSettings()

}