package com.markettwits.selfupdater.api

import com.markettwits.selfupdater.models.SelfUpdateResult

interface SelfUpdaterSourceApi {

    suspend fun checkUpdate(manual: Boolean): SelfUpdateResult

    fun getInstallSourceName(): String

    fun isSelfUpdateCanManualCheck(): Boolean
}
