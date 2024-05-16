package com.markettwits.club.dashboard.presentation.dashboard.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.bottom_bar.component.listener.BottomBarVisibilityListener
import com.markettwits.club.dashboard.domain.ClubRepository
import com.markettwits.club.dashboard.presentation.dashboard.store.ClubDashboardStore.Intent
import com.markettwits.club.dashboard.presentation.dashboard.store.ClubDashboardStore.Label
import com.markettwits.club.dashboard.presentation.dashboard.store.ClubDashboardStore.State
import com.markettwits.club.dashboard.presentation.dashboard.store.ClubDashboardStore.Message

internal class ClubDashboardStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: ClubRepository
) {

    fun create(bottomBarVisibilityListener: BottomBarVisibilityListener): ClubDashboardStore =
        ClubDashboardStoreImpl(repository, bottomBarVisibilityListener)

    private inner class ClubDashboardStoreImpl(
        private val repository: ClubRepository,
        private val bottomBarVisibilityListener: BottomBarVisibilityListener
    ) :
        ClubDashboardStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "ClubDashboardStore",
            initialState = ClubDashboardStore.State(bottomBarVisibilityListener = bottomBarVisibilityListener),
            executorFactory = { ClubDashboardExecutor(repository) },
            reducer = ClubDashboardReducer,
            bootstrapper = SimpleBootstrapper(Unit)
        )
}