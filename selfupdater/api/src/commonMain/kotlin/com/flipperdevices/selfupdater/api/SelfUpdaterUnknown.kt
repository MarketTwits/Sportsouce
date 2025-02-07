package com.flipperdevices.selfupdater.api

import com.flipperdevices.selfupdater.models.SelfUpdateResult

class SelfUpdaterUnknown : SelfUpdaterSourceApi {
    override suspend fun checkUpdate(manual: Boolean): SelfUpdateResult =
        SelfUpdateResult.NoUpdates

    override fun getInstallSourceName(): String = ""

    override fun isSelfUpdateCanManualCheck(): Boolean = false
}