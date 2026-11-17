package com.markettwits.sportsouce.review.review.presentation.component

import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.markettwits.sportsouce.review.review.presentation.store.ReviewStore
import kotlinx.coroutines.flow.StateFlow

interface ReviewComponent : LifecycleOwner {
    fun obtainEvent(event: ReviewStore.Intent)
    val value: StateFlow<ReviewStore.State>
}
