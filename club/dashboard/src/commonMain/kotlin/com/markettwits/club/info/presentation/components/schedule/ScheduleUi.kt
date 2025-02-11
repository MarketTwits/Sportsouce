package com.markettwits.club.info.presentation.components.schedule

import androidx.compose.runtime.Immutable
import com.markettwits.club.info.domain.models.Schedule

@Immutable
sealed interface ScheduleUi {
    @Immutable
    data class Header(
        val id: Int,
        val value: String,
    ) : ScheduleUi

    @Immutable
    data class Row(
        val id: Int,
        val address: String,
        val description: String,
        val kindOfSport: String,
        val startDate: String,
        val trainerFullName: String,
        val weekday: String,
        val workoutTitle: String
    ) : ScheduleUi
}

internal fun List<Schedule>.mapScheduleToBaseTable(): List<ScheduleUi> {
    val item: List<ScheduleUi> = this.map {
        ScheduleUi.Row(
            id = it.id,
            address = it.address,
            description = it.description,
            kindOfSport = it.kindOfSport,
            startDate = it.startDate,
            trainerFullName = it.trainerFullName,
            weekday = it.weekday,
            workoutTitle = it.workoutTitle
        )
    }
    item.toMutableList().addAll(
        listOf(
            ScheduleUi.Header(0, "Вид спорта"),
            ScheduleUi.Header(1, "Место проведения"),
            ScheduleUi.Header(2, "Тренер"),
            ScheduleUi.Header(3, "Дата и время"),
            ScheduleUi.Header(4, "Описание"),
            ScheduleUi.Header(5, "Записаться на пробную"),
        )
    )
    return item
}

