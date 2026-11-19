package com.markettwits.sportsouce.profile.registrations.presentation.management

import com.markettwits.sportsouce.auth.service.api.SharedUser
import com.markettwits.sportsouce.profile.cloud.model.registrations.RegistrationOption
import com.markettwits.sportsouce.profile.cloud.model.registrations.RegistrationOptions
import com.markettwits.sportsouce.profile.cloud.model.registrations.RegistrationRefundRules
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderMember
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import kotlinx.serialization.json.*
import kotlin.math.roundToLong
import kotlin.time.Clock
import kotlin.time.Instant

internal enum class RegistrationOwner(val label: String) {
    All("Все"), Mine("Мои"), Others("Другие участники")
}

internal fun StartOrderMember.isCurrentUser(user: SharedUser?): Boolean {
    user ?: return false
    participantUserId?.let { return it == user.id }
    fun String.normalized() = trim().replace(Regex("\\s+"), " ").lowercase().replace('ё', 'е')
    val userDate = user.birthday?.take(10)?.takeIf { parseDate(it) != null }
    val memberDate = birthday?.take(10)
    return user.name.isNotBlank() && user.surname.isNotBlank() &&
        user.name.normalized() == name.normalized() && user.surname.normalized() == surname.normalized() &&
        (userDate == null || memberDate.isNullOrBlank() || userDate == memberDate)
}

internal fun parseDate(value: String): LocalDate? = runCatching { LocalDate.parse(value) }.getOrNull()

private fun parseInstant(value: String): Instant? =
    runCatching { Instant.parse(value) }.getOrNull()
        ?: runCatching { LocalDateTime.parse(value).toInstant(TimeZone.UTC) }.getOrNull()
        ?: parseDate(value)?.atStartOfDayIn(TimeZone.UTC)

internal fun StartOrderInfo.canManageSlot(now: Instant = Clock.System.now()): Boolean =
    !isRefunded && (startStatusCode == 3 || parseInstant(dateStartCloud)?.let { it > now } == true)

internal fun StartOrderInfo.inlinePaymentAvailable(now: Instant = Clock.System.now()): Boolean =
    !isRefunded && (paymentCode in setOf(0, 6) || paymentCode == null && !payment.isPaid) &&
        (startStatusCode == 3 || parseInstant(dateStartCloud)?.let { it >= now } == true)

internal fun StartOrderInfo.isRegistrationPast(now: Instant = Clock.System.now()): Boolean =
    startStatusCode in setOf(6, 7) || (startStatusCode != 5 &&
        parseInstant(dateEndCloud ?: dateStartCloud)?.let { it < now } == true)

internal fun StartOrderInfo.canRefund(now: Instant = Clock.System.now()): Boolean =
    !isRefunded && paymentGroupId != null && paymentCode in setOf(1, 2, 4, 5, 7) &&
        parseInstant(dateStartCloud)?.let { it >= now } == true

internal fun RegistrationRefundRules.applicableRule(order: StartOrderInfo, now: Instant = Clock.System.now()): RegistrationRefundRules.Rule? {
    val date = parseInstant(startDate ?: order.dateStartCloud) ?: return null
    if (date < now) return null
    val days = (date - now).inWholeDays
    return rules.filter { days >= it.daysBeforeStart }.maxByOrNull { it.daysBeforeStart }
}

internal val RegistrationOptions.unpaidOptions: List<RegistrationOption>
    get() = options.filter { it.status == "unpaid" }

internal fun StartOrderInfo.hasOptionsToPay(options: RegistrationOptions?): Boolean =
    !isRefunded && !isRegistrationPast() &&
        (options?.unpaidOptions?.isNotEmpty() ?: (hasUnpaidOptions || paymentCode == 7))

internal fun RegistrationOptions.unpaidAmount(): Long? {
    val unpaid = unpaidOptions
    if (unpaid.isEmpty()) return null
    return unpaid.sumOf { it.price?.toLong() ?: return null }
}

internal fun payRegistrationOptionsLabel(amount: Long?): String =
    if (amount != null && amount > 0) "Оплатить опции · ${registrationMoney(amount)}" else "Оплатить опции"

internal fun refundAmount(kopecks: Int, percent: Int): Long = (kopecks.toLong() * percent / 100.0).roundToLong()

internal fun registrationMoney(kopecks: Long): String {
    val rubles = (kopecks / 100).toString().reversed().chunked(3).joinToString(" ").reversed()
    val cents = kopecks % 100
    return if (cents == 0L) "$rubles ₽" else "$rubles,${cents.toString().padStart(2, '0')} ₽"
}

internal fun RegistrationOption.priceFor(value: JsonElement?): Int = when {
    needsAnswer && type == "Синглселект" -> field.options.find { it.id == (value as? JsonPrimitive)?.intOrNull }?.price ?: (price ?: 0)
    needsAnswer && type == "Мультиселект" -> {
        val selection = (value as? JsonArray).orEmpty().mapNotNull { (it as? JsonPrimitive)?.intOrNull }
        field.options.filter { it.id in selection }.sumOf { it.price ?: 0 }
    }
    else -> price ?: 0
}

internal fun RegistrationOption.validValue(value: JsonElement?): Boolean = when (answerKey) {
    "bool" -> (value as? JsonPrimitive)?.booleanOrNull == true
    "number" -> (value as? JsonPrimitive)?.doubleOrNull?.isFinite() == true
    "date" -> parseDate((value as? JsonPrimitive)?.contentOrNull.orEmpty()) != null
    "singleSelect" -> field.options.any { it.id == (value as? JsonPrimitive)?.intOrNull && it.stockLeft != 0 }
    "multiSelect" -> (value as? JsonArray)?.takeIf { it.isNotEmpty() }?.all { selection ->
        field.options.any { it.id == (selection as? JsonPrimitive)?.intOrNull && it.stockLeft != 0 }
    } == true
    else -> {
        val input = (value as? JsonPrimitive)?.contentOrNull.orEmpty()
        if (type == "Время") runCatching { LocalTime.parse(input) }.isSuccess else input.isNotBlank()
    }
}

internal fun RegistrationOption.newAnswer(value: JsonElement): JsonObject = buildJsonObject {
    put("type", type)
    val answer = when (answerKey) {
        "date" -> JsonPrimitive("${(value as JsonPrimitive).content}T00:00:00.000Z")
        "string" -> {
            val input = (value as JsonPrimitive).content.trim()
            JsonPrimitive(if (type == "Время" && input.length == 5) "$input:00" else input)
        }
        else -> value
    }
    put(answerKey, answer)
}

internal fun optionsPaymentBody(selected: List<Pair<RegistrationOption, JsonElement?>>): JsonObject {
    fun item(option: RegistrationOption, optionId: Int?) = buildJsonObject {
        put("additionalFieldId", option.additionalFieldId)
        put("optionId", optionId?.let(::JsonPrimitive) ?: JsonNull)
        put("distanceId", option.distanceId)
        put("memberIndex", option.memberIndex)
    }

    fun answer(option: RegistrationOption, value: JsonElement) = buildJsonObject {
        put("additionalFieldId", option.additionalFieldId)
        put("memberStartId", option.memberStartId)
        option.newAnswer(value).forEach { (key, value) -> put(key, value) }
    }

    val multiVariants = selected.filter { (option, _) -> option.type == "Мультиселект" && option.optionId != null }
        .groupBy { (option, _) -> option.memberStartId to option.additionalFieldId }

    return buildJsonObject {
        putJsonArray("additional_fields_for_payment") {
            selected.forEach { (option, value) ->
                when {
                    option.needsAnswer && option.type == "Мультиселект" -> (value as JsonArray).forEach {
                        add(item(option, (it as JsonPrimitive).int))
                    }
                    option.needsAnswer && option.type == "Синглселект" -> add(item(option, (value as JsonPrimitive).int))
                    else -> add(item(option, option.optionId))
                }
            }
        }
        putJsonArray("answers") {
            selected.filter { it.first.needsAnswer }.forEach { (option, value) ->
                add(answer(option, requireNotNull(value)))
            }
            multiVariants.values.forEach { variants ->
                val option = variants.first().first
                val oldIds = (option.currentValue?.get("multiSelect") as? JsonArray).orEmpty()
                val newIds = variants.mapNotNull { it.first.optionId?.let(::JsonPrimitive) }
                add(answer(option, JsonArray((oldIds + newIds).distinct())))
            }
        }
    }
}

internal fun initialRegistrationPromo(order: StartOrderInfo): String =
    if (order.orderType is StartOrderType.ReRegistration) "" else order.promo.trim().lowercase()
