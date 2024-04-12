package com.markettwits.settings.api.api

import com.markettwits.settings.api.api.params.ApplicationSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeSettings(): Flow<ApplicationSettings>
    suspend fun fetchSettings(): ApplicationSettings
}

interface MutableSettingsRepository : SettingsRepository {
    suspend fun updateSettings(settings: ApplicationSettings)
}