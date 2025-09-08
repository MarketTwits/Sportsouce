package com.markettwits.sportsouce.start.presentation.start.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.IntentAction
import com.markettwits.core.errors.api.throwable.isNetworkConnectionError
import com.markettwits.core.log.LogTagProvider
import com.markettwits.core.log.errorLog
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.StateEventWithContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.core_ui.items.event.triggered
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.start.cloud.model.start.fields.Distance
import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.domain.StartRepository
import com.markettwits.sportsouce.start.domain.mapper.StartsListItemToStartItemMapper
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult
import com.markettwits.sportsouce.start.presentation.start.component.StartScreenInput
import com.markettwits.sportsouce.start.presentation.start.store.StartScreenStore.*
import com.markettwits.sportsouce.start.presentation.start.store.StartScreenStore.Label.*
import com.markettwits.sportsouce.start.presentation.start.store.StartScreenStoreFactory.Msg.TriggerEvent
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.coroutines.launch

interface StartScreenStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object OnClickBack : Intent
        data object OnClickRetry : Intent
        data object OnClickRegistration : Intent
        data object OnClickMembersResult : Intent
        data object OnClickFullAlbum : Intent
        data object OnConsumedEvent : Intent
        data object OnClickShare : Intent
        data class OnClickMembers(val members: List<StartMembersUi>) : Intent
        data class OnClickUrl(val url: String) : Intent
        data class OnClickPhone(val url: String) : Intent
        data class OnClickStartRecommended(val startId: StartsListItem) : Intent
        data class TriggerEvent(val message: String, val status: Boolean) : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val message: String = "",
        val error: Throwable? = null,
        val startItem: StartItem? = null,
        val startsRecommended: List<StartsListItem> = emptyList(),
        val event: StateEventWithContent<EventContent> = consumed(),
        val isPartialData: Boolean = false,
    )

    sealed interface Label {
        data object OnClickBack : Label
        data class OnApplyStartId(val startId: Int) : Label
        data class OnClickMembers(val startId: Int, val members: List<StartMembersUi>) : Label
        data class OnClickMembersResult(val membersResult: List<MemberResult>) : Label
        data class OnClickStartRecommended(val start: StartsListItem) : Label
        data class OnClickFullAlbum(val images: List<String>) : Label
        data class OnClickDistanceNew(
            val startId: Int,
            val distanceInfo: List<DistinctDistance>,
            val mapDistance: List<Distance>,
            val paymentDisabled: Boolean,
            val paymentType: String,
            val startTitle: String,
        ) : Label
    }
}

class StartScreenStoreFactory(
    private val storeFactory: StoreFactory,
    private val service: StartRepository,
    private val exceptionTracker: ExceptionTracker,
    private val intentAction: IntentAction
) {
    fun create(input: StartScreenInput): StartScreenStore =
        object : StartScreenStore, Store<Intent, State, Label> by storeFactory.create(
            name = "StartScreenStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { ExecutorImpl(input, exceptionTracker, intentAction) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data object Loading : Msg
        data object OnConsumedEvent : Msg
        data class TriggerEvent(val message: String, val status: Boolean) : Msg
        data class StartInfoSuccess(val data: StartItem) : Msg
        data class StartInfoFailed(val exception: Throwable) : Msg
        data class StartsRecommendedSuccess(val data: List<StartsListItem>) : Msg
        data class SetPartialStartItem(val data: StartItem) : Msg
    }

    private inner class ExecutorImpl(
        private val startInput: StartScreenInput,
        private val exceptionTracker: ExceptionTracker,
        private val intentAction: IntentAction,
    ) : CoroutineExecutor<Intent, Unit, State, Msg, Label>(), LogTagProvider {

        override val tag: String = "StartScreenExecutorImpl"

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.OnClickBack -> publish(OnClickBack)
                is Intent.OnClickMembers -> {
                    state().startItem?.let { startItem ->
                        publish(OnClickMembers(startItem.id, intent.members))
                    }
                }
                is Intent.OnClickRetry -> launch(startInput, true)
                is Intent.OnClickFullAlbum -> {
                    val images = state().startItem?.startAlbum?.flatMap { album ->
                        album.photos.map { photo -> photo.imageUrl }
                    }
                    if (!images.isNullOrEmpty()) {
                        publish(OnClickFullAlbum(images))
                    }
                }

                is Intent.OnConsumedEvent -> dispatch(Msg.OnConsumedEvent)
                is Intent.TriggerEvent -> dispatch(TriggerEvent(intent.message, intent.status))
                is Intent.OnClickUrl -> intentAction.openWebPage(intent.url)
                is Intent.OnClickPhone -> intentAction.openPhone(intent.url)
                is Intent.OnClickRegistration -> {
                    state().startItem?.let { startItem ->
                        if (startItem.regLink.isNotEmpty()) {
                            intentAction.openWebPage(startItem.regLink)
                        }
                        if (startItem.distanceInfoNew.isNotEmpty() && startItem.startStatus.code == 3) {
                            publish(
                                OnClickDistanceNew(
                                    startId = state().startItem?.id ?: 0,
                                    startTitle = startItem.title,
                                    distanceInfo = startItem.distanceInfoNew,
                                    paymentDisabled = startItem.paymentDisabled,
                                    paymentType = startItem.paymentType,
                                    mapDistance = startItem.distanceMapNew
                                )
                            )
                        }
                    }
                }

                is Intent.OnClickMembersResult -> {
                    state().startItem?.let { startItem ->
                        publish(OnClickMembersResult(startItem.membersResults))
                    }
                }

                is Intent.OnClickStartRecommended -> publish(OnClickStartRecommended(intent.startId))
                is Intent.OnClickShare -> {
                    val path = state().startItem?.let { startItem ->
                        startItem.slug.ifEmpty { startItem.id.toString() }
                    }
                    intentAction.sharePlainText("https://sportsauce.ru/starts/$path")
                }
            }
        }

        override fun executeAction(action: Unit) {
            // If input is StartsListItem, immediately show partial data
            if (startInput is StartScreenInput.Item) {
                val partialStartItem = StartsListItemToStartItemMapper.mapToPartialStartItem(startInput.item)
                dispatch(Msg.SetPartialStartItem(partialStartItem))
            }
            launch(startInput = startInput, false)
        }

        private fun launch(startInput: StartScreenInput, relaunch: Boolean) {
            val startIdString = when (startInput) {
                is StartScreenInput.Id -> startInput.startId.toString()
                is StartScreenInput.Slug -> startInput.slug
                is StartScreenInput.Item -> startInput.item.id.toString()
            }

            scope.launch {
                dispatch(Msg.Loading)
                service.start(startIdString, relaunch).fold(
                    onFailure = { exception ->
                        if (!exception.isNetworkConnectionError()) {
                            exceptionTracker.setKey(Pair("startId", startInput.toString()))
                            exceptionTracker.reportException(exception, "StartScreenStore#launch")
                            errorLog { "Fail to fetch start ${exception.message}" }
                        }
                        dispatch(Msg.StartInfoFailed(exception))
                    },
                    onSuccess = {
                        publish(OnApplyStartId(it.id))
                        dispatch(Msg.StartInfoSuccess(it))
                    }
                )
            }
            scope.launch {
                service.startsRecommended(startIdString).onSuccess {
                    dispatch(Msg.StartsRecommendedSuccess(it))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.Loading -> copy(
                isLoading = true,
                error = null
            )

            is Msg.OnConsumedEvent -> copy(
                event = consumed()
            )

            is Msg.StartInfoFailed -> copy(
                isLoading = false,
                error = msg.exception,
                isPartialData = true
            )

            is Msg.StartInfoSuccess -> copy(
                startItem = msg.data,
                isLoading = false,
                error = null,
                isPartialData = false
            )

            is Msg.StartsRecommendedSuccess -> copy(
                startsRecommended = msg.data
            )

            is Msg.SetPartialStartItem -> copy(
                startItem = msg.data,
                isPartialData = true,
                error = null
            )

            is TriggerEvent -> copy(
                event = triggered(
                    EventContent(
                        success = msg.status,
                        message = msg.message
                    )
                )
            )
        }
    }
}
