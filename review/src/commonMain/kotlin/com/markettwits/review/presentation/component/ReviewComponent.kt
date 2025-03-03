package com.markettwits.review.presentation.component

import com.markettwits.review.presentation.store.ReviewStore
import kotlinx.coroutines.flow.StateFlow

interface ReviewComponent {
    fun obtainEvent(event: ReviewStore.Intent)
    val value: StateFlow<ReviewStore.State>
}