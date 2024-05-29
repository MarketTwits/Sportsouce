package com.markettwits.start.presentation.comments.comments.component

import com.markettwits.start.presentation.comments.comments.store.StartCommentsStore
import kotlinx.coroutines.flow.StateFlow

interface StartCommentsComponent {
    val state: StateFlow<StartCommentsStore.State>
    fun obtainEvent(intent: StartCommentsStore.Intent)
}