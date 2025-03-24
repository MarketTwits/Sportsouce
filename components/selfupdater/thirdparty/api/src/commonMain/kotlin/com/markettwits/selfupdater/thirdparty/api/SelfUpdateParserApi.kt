package com.markettwits.selfupdater.thirdparty.api

interface SelfUpdateParserApi {
    fun getName(): String
    suspend fun getLastUpdate(): SelfUpdate?
}
