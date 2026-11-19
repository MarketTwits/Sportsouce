package com.markettwits.sportsouce.profile.authorized.receipts

import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkReceiptsApi
import com.markettwits.sportsouce.profile.cloud.model.receipts.PaidReceipt

interface ReceiptsRepository {
    suspend fun receipts(): List<PaidReceipt>
    suspend fun document(id: Int): String
}

internal class ReceiptsRepositoryBase(
    private val api: SportSauceNetworkReceiptsApi,
    private val auth: AuthDataSource,
) : ReceiptsRepository {
    override suspend fun receipts() = api.receipts(auth.updateToken().getOrThrow())
    override suspend fun document(id: Int) = api.document(id, auth.updateToken().getOrThrow())
}
