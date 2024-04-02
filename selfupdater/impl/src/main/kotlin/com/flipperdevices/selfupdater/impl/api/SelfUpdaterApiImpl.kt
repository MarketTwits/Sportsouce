package com.flipperdevices.selfupdater.impl.api

import com.flipperdevices.selfupdater.api.SelfUpdaterApi
import com.flipperdevices.selfupdater.api.SelfUpdaterSourceApi
import com.flipperdevices.selfupdater.models.SelfUpdateResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class SelfUpdaterApiImpl(
    private val selfUpdaterSourceApi: SelfUpdaterSourceApi
) : SelfUpdaterApi {
    private val inProgressState = MutableStateFlow(false)

    override suspend fun startCheckUpdate(manual: Boolean): SelfUpdateResult {
        if (!inProgressState.compareAndSet(expect = false, update = true)) {
            return SelfUpdateResult.IN_PROGRESS
        }
        return try {
            selfUpdaterSourceApi.checkUpdate(manual = manual)
        } catch (e: Exception) {
            return SelfUpdateResult.ERROR(e)
        } finally {
            inProgressState.emit(false)
        }
    }

    override fun getInProgressState(): StateFlow<Boolean> = inProgressState.asStateFlow()

    override fun getInstallSourceName(): String = selfUpdaterSourceApi.getInstallSourceName()

    override fun isSelfUpdateCanManualCheck(): Boolean =
        selfUpdaterSourceApi.isSelfUpdateCanManualCheck()
}
