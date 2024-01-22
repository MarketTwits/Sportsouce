package com.markettwits.start.presentation.comments.comments

import kotlinx.coroutines.flow.StateFlow

interface StartCommentsComponent {
    val state : StateFlow<StartCommentsStore.State>
    fun obtainEvent(intent : StartCommentsStore.Intent)
}