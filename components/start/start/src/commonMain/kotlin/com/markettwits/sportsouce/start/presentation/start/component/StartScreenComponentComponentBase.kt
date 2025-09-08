package com.markettwits.sportsouce.start.presentation.start.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult
import com.markettwits.sportsouce.start.presentation.start.store.StartScreenStore
import com.markettwits.sportsouce.start.presentation.start.store.StartScreenStoreFactory
import com.markettwits.sportsouce.start.register.presentation.distances.component.StartDistancesInput
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StartScreenComponentComponentBase(
    componentContext: ComponentContext,
    private val input: StartScreenInput,
    private val back: () -> Unit,
    private val registerNew: (StartDistancesInput) -> Unit,
    private val storeFactory: StartScreenStoreFactory,
    private val members: (Int, List<StartMembersUi>) -> Unit,
    private val membersResult: (List<MemberResult>) -> Unit,
    private val album: (List<String>) -> Unit,
    private val pushStart: (StartsListItem) -> Unit,
    private val onApplyStartId: (Int) -> Unit,
) : ComponentContext by componentContext, StartScreenComponent {

    private val store = instanceKeeper.getStore {
        storeFactory.create(input)
    }
    private val scope = CoroutineScope(Dispatchers.Main)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val start: StateFlow<StartScreenStore.State> = store.stateFlow

    override fun obtainEvent(intent: StartScreenStore.Intent) {
        store.accept(intent)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is StartScreenStore.Label.OnClickBack -> back()
                    is StartScreenStore.Label.OnClickMembers -> members(it.startId, it.members)
                    is StartScreenStore.Label.OnClickFullAlbum -> album(it.images)
                    is StartScreenStore.Label.OnClickMembersResult -> membersResult(it.membersResult)
                    is StartScreenStore.Label.OnClickDistanceNew -> registerNew(
                        StartDistancesInput(
                            startId = it.startId,
                            startTitle = it.startTitle,
                            paymentType = it.paymentType,
                            distance = it.distanceInfo,
                            paymentDisabled = it.paymentDisabled,
                            mapDistance = it.mapDistance,
                        )
                    )
                    is StartScreenStore.Label.OnClickStartRecommended -> pushStart(it.start)
                    is StartScreenStore.Label.OnApplyStartId -> onApplyStartId(it.startId)
                }
            }
        }
    }
}