package com.markettwits.sportsouce.profile.results.presentation.store

import app.cash.paging.PagingData
import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.sportsouce.profile.results.domain.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface MyResultsStore : Store<MyResultsStore.Intent, MyResultsStore.State, MyResultsStore.Label> {
    data class State(
        val summary: ResultsSummary? = null,
        val summaryLoading: Boolean = true,
        val summaryError: String? = null,
        val category: String? = null,
        val comparison: ResultComparison = ResultComparison.Self,
        val progress: ResultProgress? = null,
        val progressLoading: Boolean = false,
        val progressError: String? = null,
        val series: List<ResultSeries> = emptyList(),
        val seriesKey: String? = null,
        val feedCategory: String? = null,
        val table: Boolean = false,
        val count: Int? = null,
        val feedRefreshVersion: Int = 0,
        val feed: Flow<PagingData<MyResult>> = flowOf(PagingData.empty()),
    )
    sealed interface Intent {
        data object Back : Intent
        data object Refresh : Intent
        data object RetrySummary : Intent
        data object RetryProgress : Intent
        data class Category(val key: String?) : Intent
        data class Compare(val comparison: ResultComparison) : Intent
        data class Series(val key: String?) : Intent
        data class FeedCategory(val key: String?) : Intent
        data class Table(val visible: Boolean) : Intent
        data class OpenStart(val id: Int) : Intent
        data class OpenResult(val id: Int) : Intent
    }
    sealed interface Message {
        data object SummaryLoading : Message
        data class SummaryLoaded(val value: ResultsSummary, val category: String?) : Message
        data class SummaryFailed(val message: String) : Message
        data class CategoryChanged(val key: String?) : Message
        data class ComparisonChanged(val value: ResultComparison) : Message
        data object ProgressLoading : Message
        data class ProgressLoaded(val value: ResultProgress) : Message
        data class ProgressFailed(val message: String) : Message
        data class SeriesLoaded(val value: List<ResultSeries>) : Message
        data class Series(val key: String?) : Message
        data class FeedCategory(val key: String?) : Message
        data class Table(val visible: Boolean) : Message
        data object RefreshFeed : Message
        data class Feed(val items: Flow<PagingData<MyResult>>) : Message
        data class Count(val value: Int) : Message
    }
    sealed interface Label { data object Back : Label; data class OpenStart(val id: Int) : Label; data class OpenResult(val id: Int) : Label }
}
