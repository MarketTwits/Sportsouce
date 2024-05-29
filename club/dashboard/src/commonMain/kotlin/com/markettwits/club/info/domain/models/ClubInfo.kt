package com.markettwits.club.info.domain.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface ClubInfo {
    val id: Int

    @Serializable
    data class Commands(val trainers: List<Trainer>) : ClubInfo {
        override val id: Int = 0
    }

    @Serializable
    data class Questions(val questions: List<Question>) : ClubInfo {
        override val id: Int = 1
    }

    @Serializable
    data class Statistics(val statistics: List<Statistic>) : ClubInfo {
        override val id: Int = 2
    }

    @Serializable
    data class Trainings(val training: List<Training>) : ClubInfo {
        override val id: Int = 3
    }

    @Serializable
    data class Schedules(val schedule: List<Schedule>) : ClubInfo {
        override val id: Int = 4
    }
}

fun List<ClubInfo>.findFirstSchedule(): ClubInfo.Schedules? =
    this.filterIsInstance<ClubInfo.Schedules>().firstOrNull()

