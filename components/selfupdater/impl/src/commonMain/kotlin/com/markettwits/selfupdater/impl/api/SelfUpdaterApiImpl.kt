package com.markettwits.selfupdater.impl.api

import com.markettwits.selfupdater.api.SelfUpdaterApi
import com.markettwits.selfupdater.api.SelfUpdaterSourceApi
import com.markettwits.selfupdater.models.SelfUpdateResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CancellationException


class SelfUpdaterApiImpl(
    private val selfUpdaterSourceApi: SelfUpdaterSourceApi
) : SelfUpdaterApi {
    private val inProgressState = MutableStateFlow(false)

    override suspend fun startCheckUpdate(manual: Boolean): SelfUpdateResult =
        runExclusive { selfUpdaterSourceApi.checkUpdate(manual) }

    override suspend fun resumeUpdate(): SelfUpdateResult =
        runExclusive { selfUpdaterSourceApi.resumeUpdate() }

    override suspend fun installUpdate(): SelfUpdateResult =
        runExclusive { selfUpdaterSourceApi.installUpdate() }

    private suspend fun runExclusive(operation: suspend () -> SelfUpdateResult): SelfUpdateResult {
        if (!inProgressState.compareAndSet(expect = false, update = true)) {
            return SelfUpdateResult.InProgress
        }
        return try {
            operation()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            return SelfUpdateResult.Error(e)
        } finally {
            inProgressState.value = false
        }
    }

    override fun getInProgressState(): StateFlow<Boolean> = inProgressState.asStateFlow()

    override fun updateTarget() = selfUpdaterSourceApi.updateTarget()

    override fun pendingRelease() = selfUpdaterSourceApi.pendingRelease()

    override fun downloadProgress() = selfUpdaterSourceApi.downloadProgress()

    override fun getInstallSourceName(): String = selfUpdaterSourceApi.getInstallSourceName()

    override fun isSelfUpdateCanManualCheck(): Boolean =
        selfUpdaterSourceApi.isSelfUpdateCanManualCheck()
}
