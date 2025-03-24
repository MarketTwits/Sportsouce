package com.markettwits.club.dashboard.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.bottom_bar.component.listener.BottomBarVisibilityListener
import com.markettwits.club.common.domain.ClubRepository
import com.markettwits.club.dashboard.presentation.store.ClubDashboardStore.Intent
import com.markettwits.club.dashboard.presentation.store.ClubDashboardStore.Label
import com.markettwits.club.dashboard.presentation.store.ClubDashboardStore.State
import com.markettwits.crashlitics.api.tracker.ExceptionTracker

internal class ClubDashboardStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: ClubRepository,
    private val exceptionTracker: ExceptionTracker
) {

    fun create(): ClubDashboardStore =
        ClubDashboardStoreImpl(repository, exceptionTracker)

    private inner class ClubDashboardStoreImpl(
        private val repository: ClubRepository,
        private val exceptionTracker: ExceptionTracker
    ) :
        ClubDashboardStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "ClubDashboardStore",
            initialState = State(),
            executorFactory = { ClubDashboardExecutor(repository, exceptionTracker) },
            reducer = ClubDashboardReducer,
            bootstrapper = SimpleBootstrapper(Unit)
        )
}