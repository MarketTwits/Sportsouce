package com.markettwits.sportsouce.club.dashboard.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore.*
import com.markettwits.sportsouce.club.info.domain.models.*
import com.markettwits.sportsouce.club.registration.domain.RegistrationType
import kotlinx.serialization.Serializable

interface ClubDashboardStore : Store<Intent, State, Label> {

    data class State(
        val isLoading: Boolean = false,
        val error: SauceError? = null,
        val bottomSheetData: BottomSheetData = BottomSheetData(),
    )

    @Serializable
    data class BottomSheetData(
        val trainers: List<Trainer> = emptyList(),
        val trainings: List<Training> = emptyList(),
        val statistics: List<Statistic> = emptyList(),
        val questions: List<Question> = emptyList(),
        val features: List<ClubFeature> = emptyList(),
    )

    sealed interface Intent {
        data object OnClickBack : Intent
        data class OnClickRegistration(val type: RegistrationType) : Intent
        data object RetryRequest : Intent
        data object OnClickSubscriptions : Intent
        data object OnClickSchedule : Intent
        data class OpenClubInfoDetail(
            val selectedTab: com.markettwits.sportsouce.club.info.presentation.components.bottomsheet.MenuBottomSheetType,
            val bottomSheetData: BottomSheetData,
        ) : Intent
    }

    sealed interface Message {
        data object Loading : Message
        data class Failed(val error: SauceError) : Message
        data class UpdateBottomSheetData(val data: BottomSheetData) : Message
    }

    sealed interface Label {
        data object GoBack : Label
        data class OnClickRegistration(val type: RegistrationType) : Label
        data object OnClickSubscriptions : Label
        data object OnClickSchedule : Label
        data class OpenClubInfoDetail(
            val selectedTab: com.markettwits.sportsouce.club.info.presentation.components.bottomsheet.MenuBottomSheetType,
            val bottomSheetData: BottomSheetData,
        ) : Label
    }

}
