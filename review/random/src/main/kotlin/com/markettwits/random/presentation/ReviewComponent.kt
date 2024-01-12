package com.markettwits.random.presentation

import com.markettwits.random.presentation.store.ReviewStore
import kotlinx.coroutines.flow.StateFlow

interface ReviewComponent {
    fun obtainEvent(event: ReviewStore.Intent)
    val value: StateFlow<ReviewStore.State>
}