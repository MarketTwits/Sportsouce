package com.markettwits.sportsouce.club.schedule.screen

import com.markettwits.sportsouce.club.info.domain.models.Schedule
import kotlinx.datetime.*

internal enum class ClubCalendarView(val title: String) { Week("Неделя"), Month("Месяц"), List("Список") }
internal val clubWeekdays = listOf("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье")
internal val clubShortDays = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
private val months = listOf("января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря")
private val monthNames = listOf("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь")
internal fun LocalDate.clubDateLabel() = "$day ${months[month.ordinal]}"
internal fun LocalDate.clubMonthLabel() = "${monthNames[month.ordinal]} $year"
internal fun LocalDate.clubWeek(): List<LocalDate> {
    val monday = minus(dayOfWeek.ordinal, DateTimeUnit.DAY)
    return (0..6).map { monday.plus(it, DateTimeUnit.DAY) }
}
internal fun LocalDate.clubMonthGrid(): List<LocalDate> {
    val first = LocalDate(year, month, 1)
    val last = first.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
    val count = ((first.dayOfWeek.ordinal + last.day + 6) / 7) * 7
    val start = first.minus(first.dayOfWeek.ordinal, DateTimeUnit.DAY)
    return (0 until count).map { start.plus(it, DateTimeUnit.DAY) }
}
internal fun LocalDate.clubMovePeriod(view: ClubCalendarView, direction: Int): LocalDate =
    if (view == ClubCalendarView.Month) plus(direction, DateTimeUnit.MONTH) else plus(direction * 7, DateTimeUnit.DAY)
internal fun List<Schedule>.clubSessions(date: LocalDate): List<Schedule> =
    filter { it.weekday.trim().equals(clubWeekdays[date.dayOfWeek.ordinal], ignoreCase = true) }
        .sortedWith(compareBy<Schedule> { it.startDate.replace('.', ':').substringBefore(':').toIntOrNull() ?: 24 }
            .thenBy { it.startDate.replace('.', ':').substringAfter(':', "0").toIntOrNull() ?: 0 }.thenBy { it.id })
