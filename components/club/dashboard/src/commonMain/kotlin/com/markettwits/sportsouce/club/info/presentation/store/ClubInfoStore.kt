package com.markettwits.sportsouce.club.info.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore
import com.markettwits.sportsouce.club.info.domain.models.Trainer
import com.markettwits.sportsouce.club.info.domain.models.Training
import com.markettwits.sportsouce.club.info.presentation.components.bottomsheet.MenuBottomSheetType
import com.markettwits.sportsouce.club.info.presentation.store.ClubInfoStore.*
import com.markettwits.sportsouce.club.registration.domain.RegistrationType

interface ClubInfoStore : Store<Intent, State, Label> {

    data class State(
        val selectedTab: MenuBottomSheetType? = null,
        val bottomSheetData: ClubDashboardStore.BottomSheetData = ClubDashboardStore.BottomSheetData(),
        val isLoading: Boolean = false,
    )

    sealed interface Intent {

        data object Dismiss : Intent
        data class OnTrainerRegister(val trainer: Trainer) : Intent
        data class OnTrainingRegister(val training: Training) : Intent
        data object OnClickSubscribe : Intent
    }

    sealed interface Message {
        data class SetBottomSheetData(val data: ClubDashboardStore.BottomSheetData) : Message
        data class SetSelectedTab(val tab: MenuBottomSheetType?) : Message
        data class SetLoading(val isLoading: Boolean) : Message
    }

    sealed interface Label {
        data object Dismiss : Label
        data class OpenRegistration(val type: RegistrationType) : Label
        data object OpenSchedule : Label
    }
}
