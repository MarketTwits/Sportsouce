package com.markettwits.selfupdater.unknown.api

import com.markettwits.selfupdater.api.SelfUpdaterSourceApi
import com.markettwits.selfupdater.models.SelfUpdateResult


class SelfUpdaterUnknown : SelfUpdaterSourceApi {

    override suspend fun checkUpdate(manual: Boolean): SelfUpdateResult {
        return SelfUpdateResult.NoUpdates
    }

    override fun getInstallSourceName() = "Unknown"

    override fun isSelfUpdateCanManualCheck(): Boolean = false
}
