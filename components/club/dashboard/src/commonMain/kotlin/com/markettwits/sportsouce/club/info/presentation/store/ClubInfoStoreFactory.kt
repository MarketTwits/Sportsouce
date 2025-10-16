package com.markettwits.sportsouce.club.info.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore
import com.markettwits.sportsouce.club.info.presentation.components.bottomsheet.MenuBottomSheetType
import com.markettwits.sportsouce.club.info.presentation.store.ClubInfoStore.*

internal class ClubInfoStoreFactory(
    private val storeFactory: StoreFactory,
) {

    fun create(
        selectedTab: MenuBottomSheetType,
        bottomSheetData: ClubDashboardStore.BottomSheetData,
    ): ClubInfoStore = ClubInfoStoreImpl(
        selectedTab,
        bottomSheetData,
    )

    private inner class ClubInfoStoreImpl(
        private val selectedTab: MenuBottomSheetType,
        private val bottomSheetData: ClubDashboardStore.BottomSheetData,
    ) : ClubInfoStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "ClubInfoStore",
            initialState = State(
                selectedTab = selectedTab,
                bottomSheetData = bottomSheetData
            ),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { ClubInfoExecutor() },
            reducer = ClubInfoReducer
        )
}