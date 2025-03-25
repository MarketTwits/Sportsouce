package com.markettwits.sportsouce.club.info.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.sportsouce.club.info.domain.models.ClubInfo
import com.markettwits.sportsouce.club.info.presentation.store.ClubInfoStore.Intent
import com.markettwits.sportsouce.club.info.presentation.store.ClubInfoStore.Label
import com.markettwits.sportsouce.club.info.presentation.store.ClubInfoStore.State

interface ClubInfoStore : Store<Intent, State, Label> {
    data class State(
        val info: List<ClubInfo> = emptyList(),
        val currentIndex: Int = 0
    )

    sealed interface Intent {
        data object Dismiss : Intent
    }

    sealed interface Message {
        data class Loaded(val items: List<ClubInfo>) : Message
        data class ChangePage(val index: Int) : Message
    }

    sealed interface Label {
        data object Dismiss : Label
    }

}
