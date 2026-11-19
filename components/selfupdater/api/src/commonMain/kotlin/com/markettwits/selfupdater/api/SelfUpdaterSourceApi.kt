package com.markettwits.selfupdater.api

import com.markettwits.selfupdater.models.UpdateTarget
import com.markettwits.selfupdater.models.SelfUpdateResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

interface SelfUpdaterSourceApi {

    suspend fun checkUpdate(manual: Boolean): SelfUpdateResult

    suspend fun resumeUpdate(): SelfUpdateResult = SelfUpdateResult.NoUpdates

    suspend fun installUpdate(): SelfUpdateResult = SelfUpdateResult.Unsupported

    /** Validated release details retained alongside a pending download, including offline restoration. */
    fun pendingRelease(): SelfUpdateResult.SelfUpdateReady? = null

    /** Percentage of an active in-process transfer; null when no transfer is running. */
    fun downloadProgress(): Flow<Int?> = emptyFlow()

    fun updateTarget(): UpdateTarget = UpdateTarget.Android

    fun getInstallSourceName(): String

    fun isSelfUpdateCanManualCheck(): Boolean
}
