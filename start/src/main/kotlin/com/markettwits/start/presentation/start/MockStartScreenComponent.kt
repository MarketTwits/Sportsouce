package com.markettwits.start.presentation.start

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.markettwits.cloud.ext_model.DistanceInfo
import com.markettwits.start.presentation.membres.list.StartMembersUi
import com.markettwits.start.presentation.start.store.StartScreenStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MockStartScreenComponent : StartScreenComponent {
    override val start: StateFlow<StartScreenStore.State> = MutableStateFlow(StartScreenStore.State())
    override fun obtainEvent(intent: StartScreenStore.Intent)  = Unit

}