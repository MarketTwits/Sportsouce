package com.markettwits.start.presentation.result.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.start.presentation.result.model.MemberResult
import com.markettwits.start.presentation.result.store.StartMemberResultsStore.Intent
import com.markettwits.start.presentation.result.store.StartMemberResultsStore.Label
import com.markettwits.start.presentation.result.store.StartMemberResultsStore.State

interface StartMemberResultsStore : Store<Intent, State, Label> {

    data class State(
        val defaultMembersResult: List<MemberResult>,
        val visibleMembersResult: List<MemberResult>,
        val textQuery: String = ""
    )

    sealed interface Intent {
        data object OnClickGoBack : Intent
        data class OnChangeQuery(val query: String) : Intent
        data object OnClickBrush : Intent
    }

    sealed interface Message {
        data class Loaded(val membersResult: List<MemberResult>) : Message
        data class OnChangeQuery(val query: String, val membersResult: List<MemberResult>) : Message
    }

    sealed interface Label {
        data object GoBack : Label
    }

}
