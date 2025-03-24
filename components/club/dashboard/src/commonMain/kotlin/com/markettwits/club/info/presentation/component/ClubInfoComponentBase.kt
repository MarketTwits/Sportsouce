package com.markettwits.club.info.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.club.info.domain.models.ClubInfo
import com.markettwits.club.info.presentation.store.ClubInfoStore
import com.markettwits.club.info.presentation.store.ClubInfoStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class ClubInfoComponentBase(
    componentContext: ComponentContext,
    private val dismiss: () -> Unit,
    private val index: Int,
    private val items: List<ClubInfo>,
    private val storeFactory: ClubInfoStoreFactory
) : ClubInfoComponent,
    ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val store = instanceKeeper.getStore {
        storeFactory.create(index, items)
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ClubInfoStore.State> = store.stateFlow

    override fun obtainEvent(intent: ClubInfoStore.Intent) {
        store.accept(intent)
    }

    init {
        store.labels.onEach {
            when (it) {
                ClubInfoStore.Label.Dismiss -> dismiss()
            }
        }.launchIn(scope)
    }
}
