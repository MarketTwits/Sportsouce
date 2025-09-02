package com.markettwits.selfupdater.impl.api

import com.markettwits.selfupdater.api.SelfUpdaterApi
import com.markettwits.selfupdater.api.SelfUpdaterSourceApi
import com.markettwits.selfupdater.models.SelfUpdateResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class SelfUpdaterApiImpl(
    private val selfUpdaterSourceApi: SelfUpdaterSourceApi
) : SelfUpdaterApi {
    private val inProgressState = MutableStateFlow(false)

    override suspend fun startCheckUpdate(manual: Boolean): SelfUpdateResult {
        if (!inProgressState.compareAndSet(expect = false, update = true)) {
            return SelfUpdateResult.InProgress
        }
        return try {
            selfUpdaterSourceApi.checkUpdate(manual = manual)
        } catch (e: Exception) {
            return SelfUpdateResult.Error(e)
        } finally {
            inProgressState.emit(false)
        }
    }

    override fun getInProgressState(): StateFlow<Boolean> = inProgressState.asStateFlow()

    override fun getInstallSourceName(): String = selfUpdaterSourceApi.getInstallSourceName()

    override fun isSelfUpdateCanManualCheck(): Boolean =
        selfUpdaterSourceApi.isSelfUpdateCanManualCheck()
}
