package com.markettwits.sportsouce.notification.impl.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.notification.api.store.NotificationStore
import com.markettwits.sportsouce.notification.api.store.NotificationStore.*
import com.markettwits.sportsouce.notification.impl.domain.NotificationRepository

internal class NotificationStoreFactory(private val storeFactory: StoreFactory, private val repository: NotificationRepository) {
    fun create(): NotificationStore = object : NotificationStore, Store<Intent, State, Label> by storeFactory.create(
        name = "NotificationStore", initialState = State(), bootstrapper = SimpleBootstrapper(Unit),
        executorFactory = { NotificationExecutor(repository) }, reducer = NotificationReducer,
    ) {}
}
