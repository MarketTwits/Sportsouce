package com.markettwits.sportsouce.start.presentation.start.component

import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.core.decompose.componentScope
import com.markettwits.core.log.LogTagProvider
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult
import com.markettwits.sportsouce.start.presentation.start.store.StartScreenStore
import com.markettwits.sportsouce.start.presentation.start.store.StartScreenStoreFactory
import com.markettwits.sportsouce.start.register.domain.StartRegType
import com.markettwits.sportsouce.start.register.presentation.distances.component.StartDistancesInput
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StartScreenComponentBase(
    componentContext: ComponentContext,
    private val input: StartScreenInput,
    private val back: () -> Unit,
    private val registerNew: (StartDistancesInput) -> Unit,
    private val storeFactory: StartScreenStoreFactory,
    private val members: (Int, List<StartMembersUi>) -> Unit,
    private val membersResult: (Int, List<MemberResult>) -> Unit,
    private val album: (List<String>, Int) -> Unit,
    private val pushStart: (StartsListItem) -> Unit,
    private val onApplyStartId: (Int) -> Unit,
    private val onOpenStartCommentsScreen: (Int, CommentMode) -> Unit = { _, _ -> },
    private val relatedStarts: (Int, List<StartsListItem>) -> Unit,
    private val archiveStarts: (Int, List<StartsListItem>) -> Unit,
) : ComponentContext by componentContext, StartScreenComponent, LogTagProvider {

    override val tag: String = "StartScreenComponentBase"

    private val store = instanceKeeper.getStore {
        storeFactory.create(input)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val start: StateFlow<StartScreenStore.State> = store.stateFlow

    private var refreshCommentsOnResume = false

    override fun obtainEvent(intent: StartScreenStore.Intent) {
        if (intent is StartScreenStore.Intent.OpenStartCommentsScreen) refreshCommentsOnResume = true
        store.accept(intent)
    }

    init {
        lifecycle.doOnResume {
            if (refreshCommentsOnResume) {
                refreshCommentsOnResume = false
                store.accept(StartScreenStore.Intent.RefreshComments)
            }
        }
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    is StartScreenStore.Label.OnClickBack -> back()
                    is StartScreenStore.Label.OnClickMembers -> members(it.startId, it.members)
                    is StartScreenStore.Label.OnClickFullAlbum -> album(it.images, it.albumId)
                    is StartScreenStore.Label.OnClickMembersResult -> membersResult(it.startId, it.members)
                    is StartScreenStore.Label.OnClickDistanceNew -> {
                        registerNew(
                            StartDistancesInput(
                                startId = it.startId,
                                startTitle = it.startTitle,
                                paymentType = it.paymentType,
                                distance = it.distanceInfo,
                                paymentDisabled = it.paymentDisabled,
                                mapDistance = it.mapDistance,
                                regType = if (it.isReReg && it.prevOrderId != null) StartRegType.ReReg(it.prevOrderId) else StartRegType.Default,
                            )
                        )
                    }

                    is StartScreenStore.Label.OnClickStartRecommended -> pushStart(it.start)
                    is StartScreenStore.Label.OnApplyStartId -> onApplyStartId(it.startId)
                    is StartScreenStore.Label.OnOpenStartCommentsScreen -> onOpenStartCommentsScreen(
                        it.startId,
                        it.mode
                    )

                    is StartScreenStore.Label.OnClickArchive -> archiveStarts(it.startId, it.starts)
                    is StartScreenStore.Label.OnClickRelatedStarts -> relatedStarts(it.startId, it.relatedStarts)
                }
            }
        }
    }
}