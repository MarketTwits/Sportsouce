package com.markettwits.sportsouce.club.info.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore
import com.markettwits.sportsouce.club.info.presentation.components.bottomsheet.MenuBottomSheetType
import com.markettwits.sportsouce.club.info.presentation.store.ClubInfoStore
import com.markettwits.sportsouce.club.info.presentation.store.ClubInfoStoreFactory
import com.markettwits.sportsouce.club.registration.domain.RegistrationType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class ClubInfoComponentBase(
    componentContext: ComponentContext,
    private val dismiss: () -> Unit,
    private val onOpenRegistration: (RegistrationType) -> Unit,
    private val onOpenSchedule: () -> Unit,
    private val selectedTab: MenuBottomSheetType,
    private val bottomSheetData: ClubDashboardStore.BottomSheetData,
    private val storeFactory: ClubInfoStoreFactory,
) : ClubInfoComponent,
    ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val store = instanceKeeper.getStore {
        storeFactory.create(selectedTab, bottomSheetData)
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ClubInfoStore.State> = store.stateFlow

    override fun obtainEvent(intent: ClubInfoStore.Intent) {
        store.accept(intent)
    }

    init {
        store.labels.onEach { label ->
            when (label) {
                is ClubInfoStore.Label.Dismiss -> dismiss()
                is ClubInfoStore.Label.OpenRegistration -> onOpenRegistration(label.type)
                is ClubInfoStore.Label.OpenSchedule -> onOpenSchedule()
            }
        }.launchIn(scope)
    }
}
