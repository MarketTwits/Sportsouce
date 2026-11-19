package com.markettwits.sportsouce.profile.cloud.model.receipts

import kotlinx.serialization.Serializable

@Serializable
data class PaidReceipt(
    val id: Int,
    val orderNumber: String? = null,
    val startName: String? = null,
    val startDate: String? = null,
    val amount: String? = null,
    val createdAt: String? = null,
    val participants: List<String> = emptyList(),
    val receiptUrl: String? = null,
)
