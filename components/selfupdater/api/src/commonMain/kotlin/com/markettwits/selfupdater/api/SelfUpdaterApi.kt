package com.markettwits.selfupdater.api

import com.markettwits.selfupdater.models.UpdateTarget
import com.markettwits.selfupdater.models.SelfUpdateResult
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

interface SelfUpdaterApi {
    suspend fun startCheckUpdate(manual: Boolean = false): SelfUpdateResult

    /** Restore an existing update after the application returns to the foreground. */
    suspend fun resumeUpdate(): SelfUpdateResult = SelfUpdateResult.NoUpdates

    suspend fun installUpdate(): SelfUpdateResult = SelfUpdateResult.Unsupported

    /** Validated release details retained alongside a pending download, including offline restoration. */
    fun pendingRelease(): SelfUpdateResult.SelfUpdateReady? = null

    fun getInProgressState(): StateFlow<Boolean>

    fun downloadProgress(): Flow<Int?> = emptyFlow()

    fun updateTarget(): UpdateTarget = UpdateTarget.Android

    fun getInstallSourceName(): String

    fun isSelfUpdateCanManualCheck(): Boolean
}
