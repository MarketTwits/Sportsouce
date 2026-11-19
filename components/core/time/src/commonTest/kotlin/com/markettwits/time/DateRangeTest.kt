package com.markettwits.time

import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.core.time.mapDateRange
import kotlin.test.Test
import kotlin.test.assertEquals

class DateRangeTest {
    private val mapper = BaseTimeMapper()

    @Test fun formatsInclusiveRangesWithoutRepeatingMonthOrYear() {
        assertEquals("17–18 октября 2026", mapper.mapDateRange("2026-10-17T00:00:00.000Z", "2026-10-18T10:00:00.000Z"))
        assertEquals("30 октября — 2 ноября 2026", mapper.mapDateRange("2026-10-30", "2026-11-02"))
        assertEquals("31 декабря 2026 — 2 января 2027", mapper.mapDateRange("2026-12-31", "2027-01-02"))
        assertEquals("28–29 февраля 2028", mapper.mapDateRange("2028-02-28", "2028-02-29"))
    }

    @Test fun sameDayAndMissingOrInvalidEndKeepSingleDate() {
        for (end in listOf(null, "", "null", "invalid", "2026-02-30", "2026-10-16", "2026-10-17T23:59:00Z")) {
            assertEquals("17 октября 2026", mapper.mapDateRange("2026-10-17", end), "end=$end")
        }
        assertEquals("", mapper.mapDateRange("invalid", "2026-10-18"))
    }

    @Test fun usesUtcConsistentlyWithExistingDatesAndIgnoresTimeWithinSameDay() {
        assertEquals("17 октября 2026", mapper.mapDateRange("2026-10-18T01:00:00+03:00", "2026-10-17T23:00:00Z"))
        assertEquals("17–18 октября 2026", mapper.mapDateRange("2026-10-17T23:00:00Z", "2026-10-18T00:00:00Z"))
    }
}
