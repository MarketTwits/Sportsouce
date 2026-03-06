package com.markettwits.sportsouce.starts.favorites.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.sportsouce.starts.favorites.domain.StartFavoritesRepository
import com.markettwits.sportsouce.starts.favorites.presentation.store.StartsPopularStore.*
import com.markettwits.sportsouce.starts.favorites.presentation.store.StartsPopularStore.Label.OnClickBack
import com.markettwits.sportsouce.starts.favorites.presentation.store.StartsPopularStore.Label.OnClickStart
import kotlinx.coroutines.launch

internal class StartsFavoritesExecutor(
    private val repository: StartFavoritesRepository,
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.OnClickStart -> publish(OnClickStart(intent.startItem))
            is Intent.Retry -> loadFavoriteStarts(forceRefresh = true)
            is Intent.OnClickBack -> publish(OnClickBack)
            is Intent.OnClickRemoveStart -> removeStart(intent.startItem)
            is Intent.OnClickAddStart -> addStart(intent.startItem)
        }
    }

    override fun executeAction(action: Unit) {
        loadFavoriteStarts(forceRefresh = false)
    }

    private fun loadFavoriteStarts(forceRefresh: Boolean) {
        val cachedStarts = repository.favorites.value

        if (forceRefresh) {
            dispatch(Message.Loading)
        } else {
            if (cachedStarts.isNotEmpty()) {
                dispatch(Message.Loaded(cachedStarts))
            } else {
                dispatch(Message.Loading)
            }
        }

        // Fetch fresh data from network
        scope.launch {
            repository.refresh(forceRefresh = forceRefresh)
                .onSuccess { freshStarts ->
                    // Always update with fresh data from network
                    dispatch(Message.Loaded(freshStarts))
                }
                .onFailure { error ->
                    // Only show error if we don't have cached data or it's a force refresh
                    if (cachedStarts.isEmpty() || forceRefresh) {
                        dispatch(Message.Failed(error.mapToSauceError()))
                    }
                    // If we have cached data and it's not force refresh, silently fail
                }
        }
    }

    private fun removeStart(start: com.markettwits.sportsouce.starts.common.domain.StartsListItem) {
        // Mark as deleting (show progress)
        dispatch(Message.StartDeletionStarted(start.id))

        // Send delete request immediately with optimistic update
        scope.launch {
            runCatching {
                repository.remove(start)
            }.onSuccess {
                dispatch(Message.StartDeletionCompleted(start.id, success = true))
            }.onFailure {
                dispatch(Message.StartDeletionCompleted(start.id, success = false))
            }
        }
    }

    private fun addStart(start: com.markettwits.sportsouce.starts.common.domain.StartsListItem) {
        dispatch(Message.StartAdditionStarted(start.id))

        // Send add request immediately with optimistic update
        scope.launch {
            runCatching {
                repository.add(start)
            }.onSuccess {
                dispatch(Message.StartAdditionCompleted(start.id, success = true))
            }.onFailure {
                dispatch(Message.StartAdditionCompleted(start.id, success = false))
            }
        }
    }
}
