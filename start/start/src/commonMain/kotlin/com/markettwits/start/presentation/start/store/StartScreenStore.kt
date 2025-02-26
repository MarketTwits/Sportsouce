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
import com.markettwits.start_cloud.model.start.fields.DistinctDistance
import kotlinx.coroutines.launch

interface StartScreenStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class OnClickMembers(val members: List<StartMembersUi>) : Intent
        data class OnClickDistanceNew(val distance: DistinctDistance) : Intent
        data object OnClickMembersResult : Intent
        data class TriggerEvent(val message: String, val status: Boolean) : Intent
        data object OnClickBack : Intent
        data object OnClickRetry : Intent
        data object OnClickFullAlbum : Intent
        data object OnConsumedEvent : Intent
        data class OnClickUrl(val url: String) : Intent
        data class OnClickPhone(val url: String) : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val message: String = "",
        val data: StartItem? = null,
        val event: StateEventWithContent<EventContent> = consumed()
    )

    sealed interface Label {
        data class OnClickMembers(val members: List<StartMembersUi>) : Label
        data class OnClickMembersResult(val membersResult: List<MemberResult>) : Label
        data class OnClickDistanceNew(
            val startId: Int,
            val distanceInfo: List<DistinctDistance>,
            val comboId: Int?,
            val paymentDisabled: Boolean,
            val paymentType: String,
            val startTitle: String,
        ) : Label

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
        data class InfoLoaded(val data: StartItem) : Msg
        data class InfoFailed(val message: String) : Msg
    }

    private inner class ExecutorImpl(
        private val startId: Int,
        private val exceptionTracker: ExceptionTracker,
        private val intentAction: IntentAction
    ) :
        CoroutineExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.OnClickBack -> publish(Label.OnClickBack)
                is Intent.OnClickMembers -> publish(Label.OnClickMembers(intent.members))
                is Intent.OnClickRetry -> launch(startId, true)
                is Intent.OnClickFullAlbum -> {
                    val images =
                        state().data?.startAlbum?.flatMap { it.photos.map { it.imageUrl } }
                    if (!images.isNullOrEmpty())
                        publish(Label.OnClickFullAlbum(images))
                }

                is Intent.OnConsumedEvent -> dispatch(Msg.OnConsumedEvent)
                is Intent.TriggerEvent -> dispatch(Msg.TriggerEvent(intent.message, intent.status))
                is Intent.OnClickUrl -> intentAction.openWebPage(intent.url)
                is Intent.OnClickPhone -> intentAction.openPhone(intent.url)
                is Intent.OnClickDistanceNew -> {
                    state().data?.let {
                        val matchingDistance = it.distanceMapNew.find { it.id == intent.distance.id }

                        val comboId = if (matchingDistance?.combo.isNullOrEmpty()) null else matchingDistance?.id

                        val b = if (matchingDistance?.combo != null) {
                            // Если combo не null, ищем DistinctDistance по id в combo
                            it.distanceInfoNew.filter { it.id in matchingDistance.combo!! }
                        } else {
                            // Если combo null, возвращаем выбранный DistinctDistance в виде списка
                            listOf(intent.distance)
                        }

                        publish(
                            Label.OnClickDistanceNew(
                                startId = it.id,
                                distanceInfo = b,
                                paymentDisabled = it.paymentDisabled,
                                paymentType = it.paymentType,
                                comboId = comboId,
                                startTitle = it.title
                            )
                        )
                    }
                }

                is Intent.OnClickMembersResult -> state().data?.let { Label.OnClickMembersResult(it.membersResults) }
                    ?.let { publish(it) }
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
                        dispatch(Msg.InfoFailed(networkExceptionHandler(it).message.toString()))
                    },
                    onSuccess = {
                        dispatch(Msg.InfoLoaded(it))
                    }
                )
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.InfoFailed -> State(message = msg.message, isError = true)
                is Msg.InfoLoaded -> State(data = msg.data)
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
            }
    }
}
