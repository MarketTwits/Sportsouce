package com.markettwits.sportsouce.start

import com.markettwits.sportsouce.start.cloud.model.refund.StartRefundRuleRemote
import com.markettwits.sportsouce.start.cloud.model.refund.StartRefundRulesRemote
import com.markettwits.sportsouce.start.data.start.mapper.toDomain
import com.markettwits.sportsouce.start.domain.StartRefundRules
import kotlin.test.*

class StartRefundRulesMappingTest {
    private val remote = StartRefundRulesRemote("start", 318, "2026-10-25T10:00:00.000Z", listOf(
        StartRefundRuleRemote(1, 70, 30), StartRefundRuleRemote(2, 0, 0),
        StartRefundRuleRemote(3, 50, 50, "  Индивидуальные условия  "), StartRefundRuleRemote(4, 30, 14)))

    @Test fun serverPolicyIsPreservedAndSortedByDaysRatherThanPercent() {
        val rules = remote.toDomain()
        assertEquals(StartRefundRules.Source.Start, rules.source)
        assertEquals("25 октября 2026", rules.startDate)
        assertEquals(listOf(50, 70, 30, 0), rules.rules.map { it.refundPercent })
        assertEquals(listOf(50, 30, 14, 0), rules.rules.map { it.daysBeforeStart })
        assertEquals("Индивидуальные условия", rules.rules.first().description)
        assertEquals("", rules.rules.last().description)
        assertEquals(StartRefundRules.Source.Global, remote.copy(source = "global").toDomain().source)
    }

    @Test fun emptyOrUnknownPoliciesDoNotBecomeInventedGlobalRules() {
        val rules = remote.copy(source = "other", startDate = "invalid", rules = emptyList()).toDomain()
        assertEquals(StartRefundRules.Source.Unknown, rules.source)
        assertTrue(rules.rules.isEmpty())
        assertEquals("", rules.startDate)
        assertFailsWith<IllegalArgumentException> { remote.copy(rules = listOf(StartRefundRuleRemote(1, 101, 30))).toDomain() }
        assertFailsWith<IllegalArgumentException> { remote.copy(rules = listOf(StartRefundRuleRemote(1, 50, -1))).toDomain() }
    }
}
