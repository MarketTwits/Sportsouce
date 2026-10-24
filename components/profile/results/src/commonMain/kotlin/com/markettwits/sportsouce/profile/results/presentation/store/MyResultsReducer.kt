package com.markettwits.sportsouce.profile.results.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.profile.results.presentation.store.MyResultsStore.*

internal object MyResultsReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State = when (msg) {
        Message.SummaryLoading -> copy(summaryLoading = true, summaryError = null)
        is Message.SummaryLoaded -> copy(summary = msg.value, category = msg.category, summaryLoading = false, summaryError = null)
        is Message.SummaryFailed -> copy(summaryLoading = false, summaryError = msg.message)
        is Message.CategoryChanged -> copy(category = msg.key)
        is Message.ComparisonChanged -> copy(comparison = msg.value)
        Message.ProgressLoading -> copy(progress = null, progressLoading = true, progressError = null)
        is Message.ProgressLoaded -> copy(progress = msg.value, progressLoading = false, progressError = null)
        is Message.ProgressFailed -> copy(progress = null, progressLoading = false, progressError = msg.message)
        is Message.SeriesLoaded -> copy(series = msg.value)
        is Message.Series -> copy(seriesKey = msg.key)
        is Message.FeedCategory -> copy(feedCategory = msg.key)
        is Message.Table -> copy(table = msg.visible)
        Message.RefreshFeed -> copy(feedRefreshVersion = feedRefreshVersion + 1)
        is Message.Feed -> copy(feed = msg.items, count = null)
        is Message.Count -> copy(count = msg.value)
    }
}
