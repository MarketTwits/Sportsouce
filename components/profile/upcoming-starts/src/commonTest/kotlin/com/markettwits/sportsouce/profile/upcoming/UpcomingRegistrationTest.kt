package com.markettwits.sportsouce.profile.upcoming

import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPaymentStatus
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderType
import com.markettwits.sportsouce.starts.common.domain.StartStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Instant

class UpcomingRegistrationTest {
    @Test
    fun upcomingContainsPaidAndUnpaidScheduledEntriesInChronologicalOrder() {
        val first = order(2)
        val second = order(1, "2027-02-02T10:00:00Z")
        val cases = listOf(
            second, first, first,
            order(3).copy(payment = StartOrderPaymentStatus.NotPaid()),
            order(4).copy(payment = StartOrderPaymentStatus.Free()),
            order(5).copy(payment = StartOrderPaymentStatus.OnPlace()),
            order(6).copy(payment = StartOrderPaymentStatus.PaymentCancelled()),
            order(7).copy(isRefunded = true),
            order(8).copy(startStatusCode = StartStatus.ENDED.id),
            order(9).copy(startStatusCode = StartStatus.IS_PASSING.id),
            order(10, ""),
            order(11, "2027-01-28T10:00:00Z"),
            order(12).copy(startStatusCode = 99),
            order(13, "2027-01-28T10:00:00Z").copy(payment = StartOrderPaymentStatus.NotPaid()),
            order(14).copy(payment = StartOrderPaymentStatus.NotPaid(), isRefunded = true),
        )
        val now = Instant.parse("2027-01-31T10:00:00Z")
        val visible = upcomingRegistrations(cases).filter { it.expires > now }
        assertEquals(listOf(2, 3, 1), visible.map { it.order.id })
        assertEquals(listOf(2, 1), visible.filter { it.isPaid }.map { it.order.id })
    }

    @Test
    fun aParkSeriesStaysVisibleFromTheFirstDayUntilTheEndOfThePeriod() {
        val start = "2026-10-05T05:00:00Z"
        val end = "2026-12-31T05:00:00Z"
        val now = Instant.parse("2026-11-18T05:00:00Z")
        val registration = order(331, start).copy(startStatusCode = StartStatus.REGISTRATION_OPEN.id)

        assertEquals(setOf(331), missingStartDetailsIds(listOf(registration), now))
        val fromCatalog = upcomingRegistrations(listOf(registration), mapOf(331 to UpcomingStartDetails(endDate = end))).single()
        assertEquals("5 октября — 31 декабря 2026", fromCatalog.order.dateStartPreview)
        assertTrue(fromCatalog.isInProgress(now))
        assertTrue(fromCatalog.isLongRunning)
        assertEquals("43 дня", fromCatalog.countdown(now))
        assertEquals(Instant.parse(end), fromCatalog.expires)
        assertFalse(fromCatalog.isInProgress(fromCatalog.expires))

        val ongoing = registration.copy(startStatusCode = StartStatus.IS_PASSING.id, dateEndCloud = "2026-12-31")
        val dateOnlyEnd = upcomingRegistrations(listOf(ongoing)).single()
        assertTrue(dateOnlyEnd.isInProgress(Instant.parse("2026-12-31T23:59:59Z")))
        assertEquals("Сегодня", dateOnlyEnd.countdown(Instant.parse("2026-12-31T23:00:00Z")))
        assertEquals(Instant.parse("2027-01-01T00:00:00Z"), dateOnlyEnd.expires)
        assertTrue(upcomingRegistrations(listOf(ongoing.copy(startStatusCode = StartStatus.ENDED.id))).isEmpty())
    }

    @Test
    fun catalogSuppliesMissingImagesWithoutReplacingExistingCovers() {
        val now = Instant.parse("2027-01-31T10:00:00Z")
        val first = order(1).copy(dateEndCloud = "2027-02-02T10:00:00Z")
        val second = first.copy(id = 2, startId = 2, image = "https://example.com/registration.jpg")
        val expired = order(3, "2027-01-29T10:00:00Z").copy(dateEndCloud = "2027-01-30T10:00:00Z")
        val catalogImage = "https://example.com/catalog.jpg"

        assertEquals(setOf(1), missingStartDetailsIds(listOf(first, second, expired), now))
        val registrations = upcomingRegistrations(
            listOf(first, second),
            mapOf(1 to UpcomingStartDetails(image = catalogImage), 2 to UpcomingStartDetails(image = catalogImage)),
        )
        assertEquals(listOf(catalogImage, second.image), registrations.map { it.order.image })
        assertEquals(first.dateEndCloud, registrations.first().order.dateEndCloud)
    }

    @Test
    fun aLongRunningStartMustExceedOneCalendarMonth() {
        val registration = order(1, "2027-01-31T10:00:00Z")
        val exactlyOneMonth = registration.copy(dateEndCloud = "2027-02-28T10:00:00Z")
        val longerThanOneMonth = registration.copy(dateEndCloud = "2027-02-28T10:00:01Z")

        assertFalse(upcomingRegistrations(listOf(exactlyOneMonth)).single().isLongRunning)
        assertTrue(upcomingRegistrations(listOf(longerThanOneMonth)).single().isLongRunning)
        assertFalse(upcomingRegistrations(listOf(registration)).single().isLongRunning)
    }

    @Test
    fun countdownHandlesRussianPluralsAndTheLastMinute() {
        val registration = upcomingRegistrations(listOf(order(1))).single()
        assertEquals("1 день", registration.countdown(Instant.parse("2027-01-31T10:00:00Z")))
        assertEquals("2 дня 1 ч", registration.countdown(Instant.parse("2027-01-30T09:00:00Z")))
        assertEquals("11 дней", registration.countdown(Instant.parse("2027-01-21T10:00:00Z")))
        assertEquals("1 ч 35 мин", registration.countdown(Instant.parse("2027-02-01T08:25:00Z")))
        assertEquals("Меньше минуты", registration.countdown(Instant.parse("2027-02-01T09:59:59Z")))
    }

    @Test
    fun dateOnlyStartStaysVisibleThroughTheDateWithoutAnInventedStartTime() {
        val registration = upcomingRegistrations(listOf(order(1, "2027-02-01"))).single()
        val today = Instant.parse("2027-02-01T15:30:00Z")
        assertEquals("1 день", registration.countdown(Instant.parse("2027-01-31T23:30:00Z")))
        assertEquals("Сегодня", registration.countdown(today))
        assertTrue(registration.expires > today)
        assertFalse(registration.expires > Instant.parse("2027-02-02T00:00:00Z"))
    }
}

internal fun order(id: Int, date: String = "2027-02-01T10:00:00Z") = StartOrderInfo(
    id = id,
    startId = id,
    name = "Зимний забег $id",
    image = "",
    dateStartPreview = "1 февраля 2027",
    dateStartCloud = date,
    promo = "",
    payment = StartOrderPaymentStatus.Success(),
    members = emptyList(),
    orderType = StartOrderType.Default,
    startTitle = "",
    cost = "500 ₽",
    startStatusCode = StartStatus.WAITING.id,
)
