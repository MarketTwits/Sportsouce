package com.markettwits.start.presentation.comments

import kotlinx.coroutines.flow.StateFlow

interface StartCommentsComponent {
    val state : StateFlow<StartCommentsStore.State>
    fun obtainEvent(intent : StartCommentsStore.Intent)
}