package com.markettwits.start.presentation.start.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.IntentAction
import com.markettwits.cloud.exception.isNetworkConnectionError
import com.markettwits.cloud.exception.networkExceptionHandler
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.StateEventWithContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.core_ui.items.event.triggered
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.start.domain.StartItem
import com.markettwits.start.domain.StartRepository
import com.markettwits.start.presentation.membres.list.models.StartMembersUi
import com.markettwits.start.presentation.result.model.MemberResult
import com.markettwits.start.presentation.start.store.StartScreenStore.*
import com.markettwits.start_cloud.model.start.fields.Distance
import com.markettwits.start_cloud.model.start.fields.DistinctDistance
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.launch

interface StartScreenStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class OnClickMembers(val members: List<StartMembersUi>) : Intent
        data object OnClickRegistration : Intent
        data object OnClickMembersResult : Intent
        data class TriggerEvent(val message: String, val status: Boolean) : Intent
        data object OnClickBack : Intent
        data object OnClickRetry : Intent
        data object OnClickFullAlbum : Intent
        data object OnConsumedEvent : Intent
        data class OnClickUrl(val url: String) : Intent
        data class OnClickPhone(val url: String) : Intent
        data class OnClickStartRecommended(val startId: Int) : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val message: String = "",
        val startItem: StartItem? = null,
        val startsRecommended: List<StartsListItem> = emptyList(),
        val event: StateEventWithContent<EventContent> = consumed()
    )

    sealed interface Label {
        data class OnClickMembers(val members: List<StartMembersUi>) : Label
        data class OnClickMembersResult(val membersResult: List<MemberResult>) : Label
        data class OnClickDistanceNew(
            val startId: Int,
            val distanceInfo: List<DistinctDistance>,
            val mapDistance: List<Distance>,
            val paymentDisabled: Boolean,
            val paymentType: String,
            val startTitle: String,
        ) : Label

        data class OnClickStartRecommended(val startId: Int) : Label
        data object OnClickBack : Label
        data class OnClickFullAlbum(val images: List<String>) : Label
    }
}

class StartScreenStoreFactory(
    private val storeFactory: StoreFactory,
    private val service: StartRepository,
    private val exceptionTracker: ExceptionTracker,
    private val intentAction: IntentAction
) {
    fun create(startID: Int): StartScreenStore =
        object : StartScreenStore, Store<Intent, State, Label> by storeFactory.create(
            name = "StartScreenStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { ExecutorImpl(startID, exceptionTracker, intentAction) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data object Loading : Msg
        data object OnConsumedEvent : Msg
        data class TriggerEvent(val message: String, val status: Boolean) : Msg
        data class StartInfoSuccess(val data: StartItem) : Msg
        data class StartInfoFailed(val message: String) : Msg
        data class StartsRecommendedSuccess(val data: List<StartsListItem>) : Msg
    }

    private inner class ExecutorImpl(
        private val startId: Int,
        private val exceptionTracker: ExceptionTracker,
        private val intentAction: IntentAction
    ) : CoroutineExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.OnClickBack -> publish(Label.OnClickBack)
                is Intent.OnClickMembers -> publish(Label.OnClickMembers(intent.members))
                is Intent.OnClickRetry -> launch(startId, true)
                is Intent.OnClickFullAlbum -> {
                    val images =
                        state().startItem?.startAlbum?.flatMap { it.photos.map { it.imageUrl } }
                    if (!images.isNullOrEmpty())
                        publish(Label.OnClickFullAlbum(images))
                }

                is Intent.OnConsumedEvent -> dispatch(Msg.OnConsumedEvent)
                is Intent.TriggerEvent -> dispatch(Msg.TriggerEvent(intent.message, intent.status))
                is Intent.OnClickUrl -> intentAction.openWebPage(intent.url)
                is Intent.OnClickPhone -> intentAction.openPhone(intent.url)
                is Intent.OnClickRegistration -> {
                    state().startItem?.let { value ->
                        if (value.regLink.isNotEmpty()) {
                            intentAction.openWebPage(value.regLink)
                        }
                        if (value.distanceInfoNew.isNotEmpty() && value.startStatus.code == 3)
                            publish(
                                Label.OnClickDistanceNew(
                                    startId = startId,
                                    startTitle = value.title,
                                    distanceInfo = value.distanceInfoNew,
                                    paymentDisabled = value.paymentDisabled,
                                    paymentType = value.paymentType,
                                    mapDistance = value.distanceMapNew
                                )
                            )
                    }
                }

                is Intent.OnClickMembersResult -> state().startItem?.let { Label.OnClickMembersResult(it.membersResults) }
                    ?.let { publish(it) }

                is Intent.OnClickStartRecommended -> publish(Label.OnClickStartRecommended(intent.startId))
            }
        }

        override fun executeAction(action: Unit) {
            launch(startId = startId, false)
        }

        private fun launch(startId: Int, relaunch: Boolean) {
            scope.launch {
                dispatch(Msg.Loading)
                service.start(startId, relaunch).fold(
                    onFailure = {
                        if (!it.isNetworkConnectionError()) {
                            exceptionTracker.setKey(Pair("startId", startId.toString()))
                            exceptionTracker.reportException(it, "StartScreenStore#launch")
                        }
                        dispatch(Msg.StartInfoFailed(networkExceptionHandler(it).message.toString()))
                    },
                    onSuccess = {
                        dispatch(Msg.StartInfoSuccess(it))
                    }
                )
            }
            scope.launch {
                service.startsRecommended(startId).onSuccess {
                    dispatch(Msg.StartsRecommendedSuccess(it))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.StartInfoFailed -> copy(message = msg.message, isLoading = false, isError = true)
                is Msg.StartInfoSuccess -> copy(startItem = msg.data, isLoading = false, isError = false)
                is Msg.Loading -> copy(isLoading = true, isError = false)
                is Msg.OnConsumedEvent -> copy(event = consumed())
                is Msg.TriggerEvent -> copy(
                    event = triggered(
                        EventContent(
                            msg.status,
                            msg.message
                        )
                    )
                )
                is Msg.StartsRecommendedSuccess -> copy(startsRecommended = msg.data)
            }
    }
}
