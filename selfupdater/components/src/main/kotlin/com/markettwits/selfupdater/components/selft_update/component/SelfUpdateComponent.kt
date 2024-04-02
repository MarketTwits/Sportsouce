package com.markettwits.selfupdater.components.selft_update.component

import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore
import kotlinx.coroutines.flow.StateFlow

interface SelfUpdateComponent {
    val state: StateFlow<SelfUpdateStore.State>
    fun obtainEvent(intent: SelfUpdateStore.Intent)
}
