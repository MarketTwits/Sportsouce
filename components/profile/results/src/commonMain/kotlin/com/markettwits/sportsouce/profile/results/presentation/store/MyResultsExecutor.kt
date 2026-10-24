package com.markettwits.sportsouce.profile.results.presentation.store

import app.cash.paging.*
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.sportsouce.profile.results.domain.*
import com.markettwits.sportsouce.profile.results.presentation.store.MyResultsStore.*
import kotlinx.coroutines.*

internal class MyResultsExecutor(private val repository: MyResultsRepository) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    private var summaryJob: Job? = null
    private var progressJob: Job? = null
    private var progressVersion = 0
    private var feedVersion = 0
    override fun executeAction(action: Unit) { loadFeed(); loadSummary(); loadSeries() }
    override fun executeIntent(intent: Intent) {
        when (intent) {
            Intent.Back -> publish(Label.Back)
            Intent.Refresh -> { dispatch(Message.RefreshFeed); loadSummary() }
            Intent.RetrySummary -> if (!state().summaryLoading) loadSummary()
            Intent.RetryProgress -> if (!state().progressLoading) loadProgress()
            is Intent.Category -> if (intent.key != state().category && (intent.key == null || state().summary?.categories?.any { it.key == intent.key } == true)) {
                dispatch(Message.CategoryChanged(intent.key)); loadProgress()
            }
            is Intent.Compare -> if (intent.comparison != state().comparison) {
                dispatch(Message.ComparisonChanged(intent.comparison)); loadProgress()
            }
            is Intent.Series -> if (intent.key != state().seriesKey && (intent.key == null || state().series.any { it.key == intent.key })) {
                dispatch(Message.Series(intent.key))
                val allowed = state().series.find { it.key == intent.key }?.categories
                if (state().category != null && allowed != null && state().category !in allowed) dispatch(Message.CategoryChanged(allowed.firstOrNull()))
                loadProgress()
            }
            is Intent.FeedCategory -> if (intent.key != state().feedCategory) {
                dispatch(Message.FeedCategory(intent.key)); loadFeed()
            }
            is Intent.Table -> dispatch(Message.Table(intent.visible))
            is Intent.OpenResult -> if (intent.id > 0) publish(Label.OpenResult(intent.id))
            is Intent.OpenStart -> if (intent.id > 0) publish(Label.OpenStart(intent.id))
        }
    }

    private fun loadSeries() {
        scope.launch {
            try { val items = repository.series(); ensureActive(); dispatch(Message.SeriesLoaded(items)) }
            catch (cancelled: CancellationException) { throw cancelled }
            catch (_: Exception) { /* Optional grouping; the complete graph remains available. */ }
        }
    }

    private fun loadFeed() {
        val version = ++feedVersion
        val category = state().feedCategory
        val pager = Pager<Int, MyResult>(PagingConfig(pageSize = 20, initialLoadSize = 20, prefetchDistance = 4, enablePlaceholders = false)) {
            ResultsPagingSource(repository, category) { count -> scope.launch { if (version == feedVersion) dispatch(Message.Count(count)) } }
        }
        dispatch(Message.Feed(pager.flow.cachedIn(scope)))
    }

    private fun loadSummary() {
        summaryJob?.cancel()
        dispatch(Message.SummaryLoading)
        summaryJob = scope.launch {
            try {
                val value = repository.summary()
                ensureActive()
                val previous = state()
                val allowed = previous.series.find { it.key == previous.seriesKey }?.categories
                val available = value.categories.filter { allowed == null || it.key in allowed }
                val category = if (previous.summary != null && previous.category == null) null
                    else previous.category?.takeIf { key -> available.any { it.key == key } } ?: available.firstOrNull()?.key
                dispatch(Message.SummaryLoaded(value, category))
                loadProgress()
            } catch (cancelled: CancellationException) { throw cancelled }
            catch (error: Exception) { dispatch(Message.SummaryFailed(error.mapToSauceError().mapToString())) }
        }
    }

    private fun loadProgress() {
        progressJob?.cancel()
        val version = ++progressVersion
        val category = state().category

        val comparison = state().comparison
        val series = state().seriesKey
        dispatch(Message.ProgressLoading)
        progressJob = scope.launch {
            try {
                val result = repository.progress(category, comparison, series)
                ensureActive()
                if (version == progressVersion) dispatch(Message.ProgressLoaded(result))
            } catch (cancelled: CancellationException) { throw cancelled }
            catch (error: Exception) { if (version == progressVersion) dispatch(Message.ProgressFailed(error.mapToSauceError().mapToString())) }
        }
    }
}
