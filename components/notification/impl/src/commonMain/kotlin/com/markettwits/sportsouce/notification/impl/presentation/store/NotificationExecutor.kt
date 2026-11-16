package com.markettwits.sportsouce.notification.impl.presentation.store

import app.cash.paging.*
import com.arkivanov.mvikotlin.core.store.*
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.sportsouce.notification.impl.domain.*
import com.markettwits.sportsouce.notification.api.model.*
import com.markettwits.sportsouce.notification.api.store.NotificationStore.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
internal class NotificationExecutor(private val repository: NotificationRepository) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    private val filter = MutableStateFlow(NotificationFilter.All)
    private var countJob: Job? = null
    override fun executeAction(action: Unit) {
        val pages = repository.readState.map { it.sessionVersion }.distinctUntilChanged().flatMapLatest { version ->
            flow {
                coroutineScope {
                    emitAll(Pager<Int, InboxNotification>(PagingConfig(pageSize = 20, initialLoadSize = 20,
                        prefetchDistance = 5, enablePlaceholders = false)) {
                        NotificationPagingSource(repository) { scope.launch { dispatch(Message.RefreshFinished) } }
                    }
                        .flow.cachedIn(this).map { version to it })
                }
            }
        }
        dispatch(Message.Feed(combine(pages, repository.readState, filter) { (version, page), reads, selected ->
            if (version != reads.sessionVersion) PagingData.empty()
            else page.map(reads::apply).filter(selected::matches)
        }.cachedIn(scope)))
        scope.launch { repository.unreadCount.collect { dispatch(Message.Count(it)) } }
        refreshCount()
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            Intent.MessageShown -> dispatch(Message.Error(null))
            Intent.Back -> publish(Label.Back)
            Intent.SignIn -> publish(Label.SignIn)
            Intent.Refresh -> { dispatch(Message.Refresh(true)); refreshCount() }
            Intent.Resume -> { dispatch(Message.Refresh(false)); refreshCount() }
            Intent.RefreshFinished -> dispatch(Message.RefreshFinished)
            is Intent.Filter -> {
                filter.value = intent.filter
                dispatch(Message.Filter(intent.filter))
            }
            Intent.ReadAll -> if (!state().readingAll && state().pending.isEmpty()) {
                dispatch(Message.ReadingAll(true))
                scope.launch {
                    try { repository.readAll() }
                    catch (cancelled: CancellationException) { throw cancelled }
                    catch (error: Exception) { dispatch(Message.Error(error.notificationMessage())) }
                    finally { dispatch(Message.ReadingAll(false)) }
                }
            }
            is Intent.Read -> if (intent.item.id !in state().pending && !state().readingAll) {
                dispatch(Message.Pending(state().pending + intent.item.id))
                scope.launch {
                    try {
                        repository.read(intent.item)
                        ensureActive()
                        if (intent.openStart) intent.item.notification.startId?.let { publish(Label.OpenStart(it)) }
                    } catch (cancelled: CancellationException) { throw cancelled }
                    catch (error: Exception) { dispatch(Message.Error(error.notificationMessage())) }
                    finally { dispatch(Message.Pending(state().pending - intent.item.id)) }
                }
            }
        }
    }

    private fun refreshCount() {
        if (countJob?.isActive == true) return
        countJob = scope.launch {
            try { repository.refreshUnreadCount() }
            catch (cancelled: CancellationException) { throw cancelled }
            catch (_: Exception) { /* The paginated inbox exposes errors with an explicit retry. */ }
        }
    }
}
