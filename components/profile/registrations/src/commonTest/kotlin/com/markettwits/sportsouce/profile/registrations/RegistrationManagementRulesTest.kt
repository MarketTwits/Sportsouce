package com.markettwits.sportsouce.profile.registrations

import com.markettwits.sportsouce.auth.service.api.SharedUser
import com.markettwits.sportsouce.profile.cloud.model.registrations.RegistrationOption
import com.markettwits.sportsouce.profile.cloud.model.registrations.RegistrationRefundRules
import com.markettwits.sportsouce.profile.registrations.domain.*
import com.markettwits.sportsouce.profile.registrations.presentation.management.*
import kotlinx.serialization.json.*
import kotlin.test.*
import kotlin.time.Instant

class RegistrationManagementRulesTest {
    @Test
    fun ownershipPrefersServerIdentityAndChecksBirthdayForLegacyRows() {
        val user = SharedUser(42, "", "Пётр", "Тестов", "", "", birthday = "1998-12-04")
        val legacy = StartOrderMember("Петр", "Тестов", "", "", "", "", "", emptyList(), birthday = "1998-12-04")
        assertTrue(legacy.isCurrentUser(user))
        assertFalse(legacy.copy(participantUserId = 43).isCurrentUser(user))
        assertFalse(legacy.copy(birthday = "1998-12-05").isCurrentUser(user))
        assertTrue(legacy.copy(participantUserId = 42, name = "Опечатка").isCurrentUser(user))
    }

    @Test
    fun refundUsesFullDaysAndSelectsTheApplicableServerRule() {
        val rules = RegistrationRefundRules(
            startDate = "2026-12-20T05:00:00.000Z",
            rules = listOf(
                RegistrationRefundRules.Rule(0, 0),
                RegistrationRefundRules.Rule(70, 30),
                RegistrationRefundRules.Rule(30, 14),
            )
        )
        val order = StartOrderInfo(80, 30, "Тестовый старт", "", "", "2026-12-20T05:00:00Z",
            "", StartOrderPaymentStatus.Success(), emptyList(), StartOrderType.Default, "", "1700")
        assertEquals(70, rules.applicableRule(order, Instant.parse("2026-11-20T05:00:00Z"))?.refundPercent)
        assertEquals(30, rules.applicableRule(order, Instant.parse("2026-11-20T05:00:01Z"))?.refundPercent)
        assertNull(rules.applicableRule(order, Instant.parse("2026-12-20T05:00:01Z")))
        assertEquals(119_001L, refundAmount(170_001, 70))
    }

    @Test
    fun payingForMultiselectVariantsMergesExistingAnswersWithinEachMember() {
        val variant = RegistrationOption(4, 12, 30, 0, 71, 64, "Мерч", 50_000, "unpaid",
            "Мультиселект", RegistrationOption.Field("Мерч"),
            currentValue = buildJsonObject { put("multiSelect", JsonArray(listOf(JsonPrimitive(10)))) })
        val date = RegistrationOption(7, null, 30, 1, 72, null, "Дата", 1000, "available",
            "Дата", RegistrationOption.Field("Дата"))
        val body = optionsPaymentBody(listOf(
            variant to null,
            variant.copy(optionId = 13, status = "available") to null,
            date to JsonPrimitive("2026-12-01"),
        ))
        val items = body.getValue("additional_fields_for_payment").jsonArray
        assertEquals(listOf(12, 13, null), items.map { it.jsonObject["optionId"]?.jsonPrimitive?.intOrNull })
        val answers = body.getValue("answers").jsonArray.associate { it.jsonObject.getValue("memberStartId").jsonPrimitive.int to it.jsonObject }
        assertEquals(listOf(10, 12, 13), answers.getValue(71).getValue("multiSelect").jsonArray.map { it.jsonPrimitive.int })
        assertEquals("2026-12-01T00:00:00.000Z", answers.getValue(72).getValue("date").jsonPrimitive.content)
        assertFalse(date.validValue(JsonPrimitive("2026-02-30")))
    }
}
