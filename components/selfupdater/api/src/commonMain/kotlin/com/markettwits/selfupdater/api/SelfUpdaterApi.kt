package com.markettwits.selfupdater.api

import com.markettwits.selfupdater.models.SelfUpdateResult
import kotlinx.coroutines.flow.StateFlow

interface SelfUpdaterApi {
    suspend fun startCheckUpdate(manual: Boolean = false): SelfUpdateResult

    fun getInProgressState(): StateFlow<Boolean>

    fun getInstallSourceName(): String

    fun isSelfUpdateCanManualCheck(): Boolean
}
