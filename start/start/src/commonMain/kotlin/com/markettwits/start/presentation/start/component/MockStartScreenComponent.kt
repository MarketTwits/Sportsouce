package com.markettwits.start.presentation.start.component

import com.markettwits.start.presentation.start.store.StartScreenStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MockStartScreenComponent : StartScreenComponent {
    override val start: StateFlow<StartScreenStore.State> =
        MutableStateFlow(StartScreenStore.State())

    override fun obtainEvent(intent: StartScreenStore.Intent) = Unit

}