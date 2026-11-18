package com.markettwits.sportsouce.profile.upcoming

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.sportsouce.auth.cloud.model.common.AuthException
import com.markettwits.sportsouce.profile.registrations.data.StartOrderRegistrationRepository
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

internal interface UpcomingStartsStore : Store<UpcomingStartsStore.Intent, UpcomingStartsStore.State, UpcomingStartsStore.Label> {
    enum class ToolbarDestination { Search, Settings, Inbox, PhotoArchive }

    sealed interface Intent {
        data object Resume : Intent
        data object Pause : Intent
        data object Refresh : Intent
        data object ClearSession : Intent
        data object SignIn : Intent
        data object Browse : Intent
        data object OpenHistory : Intent
        data class OpenToolbar(val destination: ToolbarDestination) : Intent
        data class OpenRegistration(val id: Int) : Intent
    }

    data class State(
        val now: Instant,
        val content: Content = Content.Loading,
        val isRefreshing: Boolean = false,
    )

    sealed interface Content {
        data object Loading : Content
        data object Guest : Content
        data class Ready(val registrations: List<UpcomingRegistration>) : Content
        data class Failed(val error: SauceError) : Content
    }

    sealed interface Label {
        data class SignIn(val openHistoryAfter: Boolean) : Label
        data object Browse : Label
        data object History : Label
        data class Toolbar(val destination: ToolbarDestination) : Label
        data class Details(val order: StartOrderInfo) : Label
    }
}

internal fun upcomingStartsStore(
    repository: StartOrderRegistrationRepository,
    clock: Clock = Clock.System,
    loadStartDetails: suspend (Set<Int>) -> Map<Int, UpcomingStartDetails> = { emptyMap() },
): UpcomingStartsStore =
    object : UpcomingStartsStore, Store<UpcomingStartsStore.Intent, UpcomingStartsStore.State, UpcomingStartsStore.Label> by
        DefaultStoreFactory().create(
            name = "UpcomingStartsStore",
            initialState = UpcomingStartsStore.State(now = clock.now()),
            executorFactory = { UpcomingStartsExecutor(repository, clock, loadStartDetails) },
            reducer = UpcomingStartsReducer,
        ) {}

private sealed interface UpcomingStartsMessage {
    data class Show(val content: UpcomingStartsStore.Content) : UpcomingStartsMessage
    data class Tick(val now: Instant) : UpcomingStartsMessage
    data class Refreshing(val value: Boolean) : UpcomingStartsMessage
}

private object UpcomingStartsReducer : Reducer<UpcomingStartsStore.State, UpcomingStartsMessage> {
    override fun UpcomingStartsStore.State.reduce(msg: UpcomingStartsMessage) = when (msg) {
        is UpcomingStartsMessage.Show -> copy(content = msg.content)
        is UpcomingStartsMessage.Tick -> copy(now = msg.now)
        is UpcomingStartsMessage.Refreshing -> copy(isRefreshing = msg.value)
    }
}

private class UpcomingStartsExecutor(
    private val repository: StartOrderRegistrationRepository,
    private val clock: Clock,
    private val loadStartDetails: suspend (Set<Int>) -> Map<Int, UpcomingStartDetails>,
) : CoroutineExecutor<UpcomingStartsStore.Intent, Nothing, UpcomingStartsStore.State, UpcomingStartsMessage, UpcomingStartsStore.Label>() {
    private var loadJob: Job? = null
    private var ticker: Job? = null
    private var knownStartDetails = emptyMap<Int, UpcomingStartDetails>()

    override fun executeIntent(intent: UpcomingStartsStore.Intent) {
        when (intent) {
            UpcomingStartsStore.Intent.Resume -> {
                dispatch(UpcomingStartsMessage.Tick(clock.now()))
                startCountdown((state().content as? UpcomingStartsStore.Content.Ready)?.registrations.orEmpty())
                load(forced = false)
            }
            UpcomingStartsStore.Intent.Pause -> stop()
            UpcomingStartsStore.Intent.Refresh -> load(forced = true)
            UpcomingStartsStore.Intent.ClearSession -> {
                stop()
                knownStartDetails = emptyMap()
                dispatch(UpcomingStartsMessage.Show(UpcomingStartsStore.Content.Guest))
            }
            UpcomingStartsStore.Intent.SignIn -> publish(UpcomingStartsStore.Label.SignIn(openHistoryAfter = false))
            UpcomingStartsStore.Intent.Browse -> publish(UpcomingStartsStore.Label.Browse)
            UpcomingStartsStore.Intent.OpenHistory -> when (state().content) {
                is UpcomingStartsStore.Content.Ready -> publish(UpcomingStartsStore.Label.History)
                UpcomingStartsStore.Content.Guest -> publish(UpcomingStartsStore.Label.SignIn(openHistoryAfter = true))
                else -> Unit
            }
            is UpcomingStartsStore.Intent.OpenToolbar -> publish(UpcomingStartsStore.Label.Toolbar(intent.destination))
            is UpcomingStartsStore.Intent.OpenRegistration -> {
                val content = state().content as? UpcomingStartsStore.Content.Ready
                content?.registrations?.firstOrNull { it.order.id == intent.id && it.expires > clock.now() }?.let {
                    publish(UpcomingStartsStore.Label.Details(it.order))
                }
            }
        }
    }

    private fun stop() {
        loadJob?.cancel()
        ticker?.cancel()
        dispatch(UpcomingStartsMessage.Refreshing(false))
    }

    private fun startCountdown(registrations: List<UpcomingRegistration>) {
        ticker?.cancel()
        if (registrations.isEmpty()) return

        ticker = scope.launch {
            while (true) {
                val now = clock.now()
                dispatch(UpcomingStartsMessage.Tick(now))
                val nextChange = registrations.asSequence()
                    .map { if (it.start > now) it.start else it.expires }.filter { it > now }.minOrNull()
                    ?: break
                delay(minOf(15.seconds, nextChange - now).inWholeMilliseconds.coerceAtLeast(1))
            }
        }
    }

    private fun load(forced: Boolean) {
        loadJob?.cancel()
        loadJob = scope.launch {
            dispatch(UpcomingStartsMessage.Refreshing(forced && state().content is UpcomingStartsStore.Content.Ready))
            if (state().content !is UpcomingStartsStore.Content.Ready) {
                dispatch(UpcomingStartsMessage.Show(UpcomingStartsStore.Content.Loading))
            }
            try {
                val lookedUp = mutableSetOf<Int>()
                repository.registrations(forced).collect { orders ->
                    currentCoroutineContext().ensureActive()
                    val missingIds = missingStartDetailsIds(orders, clock.now())
                    val missing = missingIds - lookedUp
                    if (missing.isNotEmpty()) {
                        try {
                            val details = loadStartDetails(missing)
                            currentCoroutineContext().ensureActive()
                            knownStartDetails = (knownStartDetails - missing) + details
                            lookedUp.addAll(missing)
                        } catch (error: CancellationException) {
                            throw error
                        } catch (_: Exception) {
                            // The dashboard can still show registrations when the catalog is unavailable.
                        }
                    }
                    currentCoroutineContext().ensureActive()
                    val scheduled = upcomingRegistrations(orders, knownStartDetails)
                    dispatch(UpcomingStartsMessage.Tick(clock.now()))
                    dispatch(UpcomingStartsMessage.Show(UpcomingStartsStore.Content.Ready(scheduled)))
                    startCountdown(scheduled)
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                currentCoroutineContext().ensureActive()
                ticker?.cancel()
                val isGuest = error is AuthException ||
                    (error is ClientRequestException && error.response.status == HttpStatusCode.Unauthorized)
                val content = if (isGuest) UpcomingStartsStore.Content.Guest
                else UpcomingStartsStore.Content.Failed(error.mapToSauceError())
                dispatch(UpcomingStartsMessage.Show(content))
            } finally {
                if (currentCoroutineContext()[Job]?.isActive == true) dispatch(UpcomingStartsMessage.Refreshing(false))
            }
        }
    }
}
