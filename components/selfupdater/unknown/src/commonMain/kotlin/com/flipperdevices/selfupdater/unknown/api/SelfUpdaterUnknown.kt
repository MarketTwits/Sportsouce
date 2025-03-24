package com.flipperdevices.selfupdater.unknown.api

import com.flipperdevices.selfupdater.api.SelfUpdaterSourceApi
import com.flipperdevices.selfupdater.models.SelfUpdateResult


class SelfUpdaterUnknown : SelfUpdaterSourceApi {

    override suspend fun checkUpdate(manual: Boolean): SelfUpdateResult {
        return SelfUpdateResult.NoUpdates
    }

    override fun getInstallSourceName() = "Unknown"

    override fun isSelfUpdateCanManualCheck(): Boolean = false
}
