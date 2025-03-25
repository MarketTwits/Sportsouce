package com.markettwits.sportsouce.start.presentation.comments.component

import com.markettwits.sportsouce.start.presentation.comments.store.StartCommentsStore
import kotlinx.coroutines.flow.StateFlow

interface StartCommentsComponent {

    val state: StateFlow<StartCommentsStore.State>

    fun obtainEvent(intent: StartCommentsStore.Intent)
}