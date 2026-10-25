package com.markettwits.sportsouce.start.cloud.model.refund

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartRefundRulesRemote(
    val source: String,
    @SerialName("start_id") val startId: Int,
    @SerialName("start_date") val startDate: String? = null,
    val rules: List<StartRefundRuleRemote>,
)

@Serializable
data class StartRefundRuleRemote(
    val id: Int,
    @SerialName("refund_percent") val refundPercent: Int,
    @SerialName("days_before_start") val daysBeforeStart: Int,
    val description: String? = null,
)
