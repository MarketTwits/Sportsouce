package com.markettwits.sportsouce.club

import com.markettwits.sportsouce.club.schedule.screen.*
import kotlinx.datetime.*
import kotlin.test.*

class ClubCalendarTest {
    @Test fun recurringWeekMatchesWebsiteAcrossMonthAndYearBoundaries() {
        val week = LocalDate(2026, 10, 20).clubWeek()
        assertEquals(LocalDate(2026, 10, 19), week.first())
        assertEquals(LocalDate(2026, 10, 25), week.last())
        val boundary = LocalDate(2027, 1, 1).clubWeek()
        assertEquals(LocalDate(2026, 12, 28), boundary.first())
        assertEquals(LocalDate(2027, 1, 3), boundary.last())
        assertEquals(DayOfWeek.MONDAY, boundary.first().dayOfWeek)
    }

    @Test fun monthGridIncludesEveryDayAndPadsCompleteWeeksIncludingLeapFebruary() {
        for (year in listOf(2024, 2026)) for (month in 1..12) {
            val date = LocalDate(year, month, 1)
            val days = date.clubMonthGrid()
            assertEquals(0, days.size % 7)
            assertEquals(DayOfWeek.MONDAY, days.first().dayOfWeek)
            assertEquals(DayOfWeek.SUNDAY, days.last().dayOfWeek)
            assertEquals(days.size, days.distinct().size)
            assertEquals(date.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY).day,
                days.count { it.month == date.month })
        }
        assertEquals(LocalDate(2024, 2, 29), LocalDate(2024, 1, 31).clubMovePeriod(ClubCalendarView.Month, 1))
    }

    @Test fun agendaKeepsParallelSessionsAndOrdersHoursNumericallyForEveryRecurringWeek() {
        val sessions = listOf(session(49, 13, "Бег").copy(startDate = "9:00"),
            session(50, 13, "Бег").copy(startDate = "12:00"), session(51, 12, "Лыжи").copy(startDate = "09.30"),
            session(52, 12, "Лыжи").copy(startDate = "12:00"))
        assertEquals(listOf(49, 51, 50, 52), sessions.clubSessions(LocalDate(2026, 10, 20)).map { it.id })
        assertEquals(sessions.clubSessions(LocalDate(2026, 10, 20)), sessions.clubSessions(LocalDate(2026, 10, 27)))
        assertTrue(sessions.clubSessions(LocalDate(2026, 10, 21)).isEmpty())
    }
}
