package com.markettwits.sportsouce.review.review.presentation.component

import com.markettwits.sportsouce.review.review.presentation.store.ReviewStore
import kotlinx.coroutines.flow.StateFlow

interface ReviewComponent {
    fun obtainEvent(event: ReviewStore.Intent)
    val value: StateFlow<ReviewStore.State>
}