package com.markettwits.review.presentation

import com.markettwits.news_list.presentation.store.NewsStore
import com.markettwits.review.presentation.store.ReviewStore
import kotlinx.coroutines.flow.StateFlow

interface ReviewComponent {
    fun obtainEvent(event: ReviewStore.Intent)
    val value: StateFlow<ReviewStore.State>
}