package com.markettwits.club.info.domain.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface ClubInfo {
    val id: Int

    data class Commands(val trainers: List<Trainer>) : ClubInfo {
        override val id: Int = 0
    }

    data class Questions(val questions: List<Question>) : ClubInfo {
        override val id: Int = 1
    }

    data class Statistics(val statistics: List<Statistic>) : ClubInfo {
        override val id: Int = 2
    }

    data class Trainings(val training: List<Training>) : ClubInfo {
        override val id: Int = 3
    }
}
