package com.markettwits.start.presentation.result.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.start.domain.StartRepository
import com.markettwits.start.presentation.result.model.MemberResult
import com.markettwits.start.presentation.result.store.StartMemberResultsStore.Intent
import com.markettwits.start.presentation.result.store.StartMemberResultsStore.Label
import com.markettwits.start.presentation.result.store.StartMemberResultsStore.State
import com.markettwits.start.presentation.result.store.StartMemberResultsStore.Message
import kotlinx.coroutines.launch

class StartMemberResultsExecutor : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnClickGoBack -> publish(Label.GoBack)
            is Intent.OnChangeQuery -> onChangeQuery(intent.query)
            is Intent.OnClickBrush -> onChangeQuery("")
        }
    }

    private fun onChangeQuery(query: String) {
        dispatch(Message.OnChangeQuery(query, filterMembers(state().defaultMembersResult, query)))
    }

    private fun filterMembers(defaultMembersResult: List<MemberResult>, query: String): List<MemberResult> {
        val normalizedQuery = query.lowercase().replace(" ", "")
        val filteredResults = defaultMembersResult.filter { member ->
            member.name.lowercase().replace(" ", "").contains(normalizedQuery)
        }
        return filteredResults
    }
}
