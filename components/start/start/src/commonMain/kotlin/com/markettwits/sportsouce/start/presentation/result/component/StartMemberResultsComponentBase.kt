package com.markettwits.sportsouce.start.presentation.result.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.core.decompose.componentScope
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarComponentHandler
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarVisibilityStrategy
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult
import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStore
import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StartMemberResultsComponentBase(
    componentContext: ComponentContext,
    storeFactory: StartMemberResultsStoreFactory,
    memberResult: List<MemberResult>,
    private val goBack: () -> Unit,
) : StartMemberResultsComponent, BottomBarComponentHandler(), ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        storeFactory.create(memberResult)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<StartMemberResultsStore.State> = store.stateFlow

    override fun obtainEvent(intent: StartMemberResultsStore.Intent) {
        store.accept(intent)
    }

    init {
        subscribeOnBottomBar(BottomBarVisibilityStrategy.AlwaysInvisible)
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    is StartMemberResultsStore.Label.GoBack -> goBack()
                }
            }
        }
    }
}
