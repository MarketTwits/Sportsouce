package com.markettwits.sportsouce.start.presentation.result.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.core.decompose.componentScope
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarComponentHandler
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarVisibilityStrategy
import com.markettwits.sportsouce.start.domain.StartRepository
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult
import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStore
import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StartMemberResultsComponentBase(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    repository: StartRepository,
    memberResult: List<MemberResult>,
    startId: Int,
    private val goBack: () -> Unit,
) : StartMemberResultsComponent, BottomBarComponentHandler(), ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        StartMemberResultsStoreFactory(
            storeFactory = storeFactory,
            repository = repository,
            startId = startId
        ).create(memberResult)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<StartMemberResultsStore.State> = store.stateFlow

    private val _filterValue = MutableStateFlow("")
    override val filterValue: StateFlow<String> = _filterValue

    override fun obtainEvent(intent: StartMemberResultsStore.Intent) {
        store.accept(intent)
    }

    override fun handleTextFiled(value: String) {
        _filterValue.value = value
        obtainEvent(StartMemberResultsStore.Intent.OnChangeQuery(value))
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
        store.accept(StartMemberResultsStore.Intent.Init)
    }
}
