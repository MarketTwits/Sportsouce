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
import com.markettwits.core.log.infoLog
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.StateEventWithContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.core_ui.items.event.triggered
import com.markettwits.core_ui.items.extensions.isLooseUrl
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.start.cloud.model.start.fields.Distance
import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.domain.StartRepository
import com.markettwits.sportsouce.start.domain.mapper.StartsListItemToStartItemMapper
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult
import com.markettwits.sportsouce.start.presentation.start.component.StartFavoriteState
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
        data class OnClickFullAlbum(val album: StartItem.Album) : Intent
        data object OnConsumedEvent : Intent
        data object OnClickShare : Intent
        data class OnClickMembers(val members: List<StartMembersUi>) : Intent
        data class OnClickUrl(val url: String) : Intent
        data class OnClickPhone(val url: String) : Intent
        data class OnClickStartRecommended(val startId: StartsListItem) : Intent
        data class TriggerEvent(val message: String, val status: Boolean) : Intent
        data class OpenStartCommentsScreen(val mode: com.markettwits.sportsouce.start.presentation.start.component.CommentMode) :
            Intent

        data object OnClickRelatedStarts : Intent
        data object OnClickFavorite : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val isPartialData: Boolean = false,
        val message: String = "",
        val error: Throwable? = null,
        val startItem: StartItem? = null,
        val startsRecommended: List<StartsListItem> = emptyList(),
        val startsSeries: List<StartsListItem> = emptyList(),
        val favoriteState: StartFavoriteState = StartFavoriteState.Loading(),
        val event: StateEventWithContent<EventContent> = consumed(),
    )

    sealed interface Label {
        data object OnClickBack : Label
        data class OnClickRelatedStarts(val startId: Int, val relatedStarts: List<StartsListItem>) : Label
        data class OnApplyStartId(val startId: Int) : Label
        data class OnClickMembers(val startId: Int, val members: List<StartMembersUi>) : Label
        data class OnClickMembersResult(val startId: Int, val members: List<MemberResult>) : Label
        data class OnClickStartRecommended(val start: StartsListItem) : Label
        data class OnClickFullAlbum(val images: List<String>) : Label
        data class OnClickDistanceNew(
            val startId: Int,
            val distanceInfo: List<DistinctDistance>,
            val mapDistance: List<Distance>,
            val paymentDisabled: Boolean,
            val paymentType: String,
            val startTitle: String,
            val isReReg: Boolean = false,
            val prevOrderId: Int?,
        ) : Label

        data class OnOpenStartCommentsScreen(
            val startId: Int,
            val mode: com.markettwits.sportsouce.start.presentation.start.component.CommentMode,
        ) : Label
    }
}

class StartScreenStoreFactory(
    private val storeFactory: StoreFactory,
    private val service: StartRepository,
    private val exceptionTracker: ExceptionTracker,
    private val intentAction: IntentAction,
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
        data class StartsSeriesSuccess(val data: List<StartsListItem>) : Msg
        data class SetPartialStartItem(val data: StartItem) : Msg
        data class StartFavoriteUpdated(val state: StartFavoriteState) : Msg
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
                    val images = intent.album.photos.map { album ->
                        album.imageUrl
                    }
                    if (images.isNotEmpty()) {
                        publish(OnClickFullAlbum(images))
                    }
                }

                is Intent.OnConsumedEvent -> dispatch(Msg.OnConsumedEvent)
                is Intent.TriggerEvent -> dispatch(TriggerEvent(intent.message, intent.status))
                is Intent.OnClickUrl -> intentAction.openWebPage(intent.url)
                is Intent.OnClickPhone -> intentAction.openPhone(intent.url)
                is Intent.OnClickRegistration -> {
                    state().startItem?.let { startItem ->
                        when {
                            startItem.isExternalLinkRegistration() -> {
                                intentAction.openWebPage(startItem.regLink)
                            }

                            startItem.isDistanceRegistration() -> {
                                publish(
                                    startItem.toOnClickDistanceNew(
                                        isReReg = false,
                                    )
                                )
                            }

                            else -> {
                                dispatch(TriggerEvent("Не удалось найти подходящий старт", false))
                                errorLog { "Can't registration with startItem Id:${startItem.id}" }
                            }
                        }
                    }
                }

                is Intent.OnClickMembersResult -> {
                    state().startItem?.let { startItem ->
                        publish(
                            OnClickMembersResult(
                                startItem.id,
                                startItem.membersResults
                            )
                        )
                    }
                }

                is Intent.OnClickStartRecommended -> publish(OnClickStartRecommended(intent.startId))

                is Intent.OnClickShare -> {
                    val path = state().startItem?.let { startItem ->
                        startItem.slug.ifEmpty { startItem.id.toString() }
                    }
                    intentAction.sharePlainText("https://sportsauce.ru/starts/$path")
                }

                is Intent.OpenStartCommentsScreen -> {
                    state().startItem?.let { startItem ->
                        publish(OnOpenStartCommentsScreen(startItem.id, intent.mode))
                    }
                }

                is Intent.OnClickRelatedStarts -> {
                    state().let { state ->
                        if (state.startsSeries.isNotEmpty() || state.startItem != null) {
                            publish(
                                OnClickRelatedStarts(
                                    state.startItem?.id ?: 0,
                                    state.startsSeries
                                )
                            )
                        }
                    }
                }

                Intent.OnClickFavorite -> scope.launch {
                    onClickToFavorite()
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
                is StartScreenInput.ReReg -> startInput.startId.toString()
            }

            infoLog { "launch called with startIdString: $startIdString, relaunch: $relaunch" }
            scope.launch {
                dispatch(Msg.Loading)
                infoLog { "Calling service.start for startId: $startIdString" }
                service.start(startIdString, relaunch).fold(
                    onFailure = { exception ->
                        if (!exception.isNetworkConnectionError()) {
                            exceptionTracker.setKey(Pair("startId", startInput.toString()))
                            exceptionTracker.reportException(exception, "StartScreenStore#launch")
                            errorLog { "Fail to fetch start ${exception.message}" }
                        }
                        dispatch(Msg.StartInfoFailed(exception))
                    },
                    onSuccess = { startItem ->
                        infoLog { "service.start onSuccess for startId: ${startItem.id}, title: ${startItem.title}" }
                        publish(OnApplyStartId(startItem.id))
                        getFavoriteStatus(startItem.id)
                        dispatch(Msg.StartInfoSuccess(startItem))
                        handleReRegistrationIfNeeded(startInput, startItem)
                        getSeriesStarts(startItem.startSeries)
                    }
                )
            }
            getRecommendedStarts(startIdString)
        }

        private fun onClickToFavorite() = scope.launch {
            val favorite = state().favoriteState
            dispatch(
                Msg.StartFavoriteUpdated(
                    StartFavoriteState.Loading(
                        favorite.isFavorite
                    )
                )
            )
            state().startItem?.let { item ->
                if (favorite is StartFavoriteState.Default) {
                    when (favorite.isFavorite) {
                        true -> service.startRemoveFromFavorites(item)
                        false -> service.startAddToFavorite(item)
                    }.onSuccess {
                        dispatch(Msg.StartFavoriteUpdated(StartFavoriteState.Default(it)))
                    }.onFailure {
                        dispatch(Msg.StartFavoriteUpdated(favorite))
                        dispatch(TriggerEvent(it.message.toString(), false))
                    }
                }
            }
        }

        private fun getFavoriteStatus(startId: Int) {
            scope.launch {
                dispatch(Msg.StartFavoriteUpdated(StartFavoriteState.Loading()))
                service.isStartInFavorite(startId).onSuccess {
                    dispatch(Msg.StartFavoriteUpdated(StartFavoriteState.Default(it)))
                }.onFailure {
                    dispatch(Msg.StartFavoriteUpdated(StartFavoriteState.Default(false)))
                }
            }
        }

        private fun getRecommendedStarts(startIdString: String) {
            scope.launch {
                service.startsRecommended(startIdString).onSuccess {
                    dispatch(Msg.StartsRecommendedSuccess(it))
                }
            }
        }

        private fun getSeriesStarts(startSeries: StartItem.StartSeries) {
            if (startSeries is StartItem.StartSeries.Value)
                scope.launch {
                    service.startsSeries(startSeries.id).onSuccess {
                        dispatch(Msg.StartsSeriesSuccess(it))
                    }
                }
        }

        private fun handleReRegistrationIfNeeded(input: StartScreenInput, startItem: StartItem) {
            if (input is StartScreenInput.ReReg) {
                when {
                    startItem.isExternalLinkRegistration() -> {
                        dispatch(TriggerEvent("Регистрация доступна только через внешнюю ссылку", false))
                    }

                    startItem.isDistanceRegistration() -> {
                        publish(
                            startItem.toOnClickDistanceNew(
                                isReReg = true,
                                prevOrderId = input.orderId
                            )
                        )
                    }

                    else -> {
                        dispatch(TriggerEvent("Регистрация на данный старт закрыта", false))
                    }
                }
            } else {
                errorLog { "Input is not ReReg, it's: ${input::class.simpleName}" }
            }
        }
    }

    private fun StartItem.isExternalLinkRegistration(): Boolean {
        return regLink.trim().isNotEmpty() && !regOnSite && regLink.isLooseUrl()
    }

    private fun StartItem.isDistanceRegistration(): Boolean {
        return distanceInfoNew.isNotEmpty() && startStatus.code == 3
    }

    private fun StartItem.toOnClickDistanceNew(isReReg: Boolean, prevOrderId: Int? = null): OnClickDistanceNew =
        OnClickDistanceNew(
            startId = id,
            startTitle = title,
            distanceInfo = distanceInfoNew,
            paymentDisabled = paymentDisabled,
            paymentType = paymentType,
            mapDistance = distanceMapNew,
            isReReg = isReReg,
            prevOrderId = prevOrderId,
        )

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

            is Msg.StartsSeriesSuccess -> copy(
                startsSeries = msg.data
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

            is Msg.StartFavoriteUpdated -> copy(
                favoriteState = msg.state
            )
        }
    }
}
