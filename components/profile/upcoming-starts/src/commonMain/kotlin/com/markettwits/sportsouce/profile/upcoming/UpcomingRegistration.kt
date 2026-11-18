package com.markettwits.sportsouce.profile.upcoming

import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.core.time.mapDateRange
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPaymentStatus
import com.markettwits.sportsouce.starts.common.domain.StartStatus
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

internal data class UpcomingRegistration(
    val order: StartOrderInfo,
    val start: Instant,
    val expires: Instant = start,
    val hasStartTime: Boolean = true,
    val endsAt: Instant? = null,
    val hasEndTime: Boolean = true,
) {
    val isPaid: Boolean
        get() = order.isConfirmedPayment

    val isLongRunning: Boolean
        get() = endsAt != null && endsAt > start.plus(1, DateTimeUnit.MONTH, TimeZone.UTC)

    val distances: List<String>
        get() = order.members.map { it.distanceName.trim() }.filter(String::isNotEmpty).distinct()

    fun isInProgress(now: Instant): Boolean = endsAt != null && now >= start && now < expires

    fun countdown(now: Instant): String {
        val inProgress = isInProgress(now)
        val target = if (inProgress) requireNotNull(endsAt) else start
        val remaining = target - now
        if (if (inProgress) !hasEndTime else !hasStartTime) {
            val today = now.toLocalDateTime(TimeZone.UTC).date.atStartOfDayIn(TimeZone.UTC)
            val days = (target - today).inWholeDays
            return if (days <= 0) "Сегодня" else daysLabel(days)
        }
        if (remaining < 1.minutes) return "Меньше минуты"

        val days = remaining.inWholeDays
        val hours = remaining.inWholeHours % 24
        val minutes = remaining.inWholeMinutes % 60
        return when {
            days > 0 -> if (hours == 0L) daysLabel(days) else "${daysLabel(days)} $hours ч"
            hours > 0 -> if (minutes == 0L) "$hours ч" else "$hours ч $minutes мин"
            else -> "$minutes мин"
        }
    }
}

internal fun upcomingRegistrations(
    orders: List<StartOrderInfo>,
    startDetails: Map<Int, UpcomingStartDetails> = emptyMap(),
): List<UpcomingRegistration> = orders
    .asSequence()
    .filter { it.canAppearInUpcoming }
    .mapNotNull { order ->
        val start = order.dateStartCloud.toRegistrationDate() ?: return@mapNotNull null
        val storedEnd = order.dateEndCloud?.toRegistrationDate()?.takeIf { it.exclusiveEnd > start.instant }
        val details = startDetails[order.startId]
        val fallbackEndRaw = details?.endDate?.takeIf { storedEnd == null }
        val fallbackEnd = fallbackEndRaw?.toRegistrationDate()?.takeIf { it.exclusiveEnd > start.instant }
        val end = storedEnd ?: fallbackEnd
        if (order.startStatusCode == StartStatus.IS_PASSING.id && end == null) return@mapNotNull null
        val displayedEnd = fallbackEndRaw.takeIf { fallbackEnd != null }
        val displayedOrder = order.copy(
            image = order.image.ifBlank { details?.image.orEmpty() },
            dateEndCloud = displayedEnd ?: order.dateEndCloud,
            dateStartPreview = displayedEnd?.let { dateFormatter.mapDateRange(order.dateStartCloud, it) } ?: order.dateStartPreview,
        )
        UpcomingRegistration(displayedOrder, start.instant, end?.exclusiveEnd ?: start.exclusiveEnd,
            hasStartTime = start.hasTime, endsAt = end?.instant, hasEndTime = end?.hasTime ?: true)
    }
    .distinctBy { it.order.id }
    .sortedWith(compareBy<UpcomingRegistration> { it.start }.thenBy { it.order.id })
    .toList()

internal fun missingStartDetailsIds(orders: List<StartOrderInfo>, now: Instant): Set<Int> = orders
    .asSequence()
    .filter { it.canAppearInUpcoming }
    .mapNotNull { order ->
        val start = order.dateStartCloud.toRegistrationDate() ?: return@mapNotNull null
        val end = order.dateEndCloud?.toRegistrationDate()?.takeIf { it.exclusiveEnd > start.instant }
        order.startId.takeIf { it > 0 && (end == null || (end.exclusiveEnd > now && order.image.isBlank())) }
    }
    .toSet()

private val dateFormatter = BaseTimeMapper()

private data class RegistrationDate(val instant: Instant, val exclusiveEnd: Instant, val hasTime: Boolean)

private fun String.toRegistrationDate(): RegistrationDate? = try {
    val value = trim()
    if (value.length == 10) {
        val day = LocalDate.parse(value)
        RegistrationDate(day.atStartOfDayIn(TimeZone.UTC), day.plus(1, DateTimeUnit.DAY).atStartOfDayIn(TimeZone.UTC), false)
    } else {
        val instant = Instant.parse(value)
        RegistrationDate(instant, instant, true)
    }
} catch (_: IllegalArgumentException) {
    null
}

private val StartOrderInfo.canAppearInUpcoming: Boolean
    get() = !isRefunded && (isConfirmedPayment || payment is StartOrderPaymentStatus.NotPaid) &&
        (startStatusCode?.let(scheduledStatuses::contains) ?: true)

private val StartOrderInfo.isConfirmedPayment: Boolean
    get() = payment is StartOrderPaymentStatus.Success && payment.isPaid

private val scheduledStatuses = setOf(
    StartStatus.PENDING.id, StartStatus.ANNOUNCEMENT.id, StartStatus.REGISTRATION_OPEN.id, StartStatus.WAITING.id,
    StartStatus.IS_PASSING.id,
)

private fun daysLabel(days: Long): String = "$days " + when {
    days % 100 in 11..14 -> "дней"
    days % 10 == 1L -> "день"
    days % 10 in 2..4 -> "дня"
    else -> "дней"
}
