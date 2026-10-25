package com.markettwits.sportsouce.start.data.start.mapper

import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.core.time.mapDateRange
import com.markettwits.sportsouce.start.cloud.model.refund.StartRefundRulesRemote
import com.markettwits.sportsouce.start.domain.StartRefundRules

internal fun StartRefundRulesRemote.toDomain(): StartRefundRules {
    require(rules.all { it.refundPercent in 0..100 && it.daysBeforeStart >= 0 })
    return StartRefundRules(
        startId = startId,
        startDate = startDate?.let { BaseTimeMapper().mapDateRange(it) }.orEmpty(),
        source = when (source) {
            "global" -> StartRefundRules.Source.Global
            "start" -> StartRefundRules.Source.Start
            else -> StartRefundRules.Source.Unknown
        },
        rules = rules.sortedByDescending { it.daysBeforeStart }.map {
            StartRefundRules.Rule(it.id, it.refundPercent, it.daysBeforeStart, it.description?.trim().orEmpty())
        },
    )
}
