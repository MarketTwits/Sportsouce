package com.markettwits.start.register.presentation.order.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.start.register.presentation.order.domain.interactor.OrderInteractor
import com.markettwits.start.register.presentation.order.presentation.store.OrderStore.Intent
import com.markettwits.start.register.presentation.order.presentation.store.OrderStore.Label
import com.markettwits.start.register.presentation.order.presentation.store.OrderStore.State

class OrderStoreFactory(
    private val storeFactory: StoreFactory,
    private val interactor: OrderInteractor,
    private val handle: OrderStoreExecutorHandle
) {
    fun create(
        state: State,
        distanceInfo: DistanceItem,
        startTitle: String,
        starId: Int,
        paymentType: String,
        paymentDisabled: Boolean
    ): OrderStore = object : OrderStore, Store<Intent, State, Label> by storeFactory.create(
        name = "OrderStoreStore",
        initialState = state,
        bootstrapper = OrderBootstrapper(
            state.orderStatement == null,
            interactor,
            startTitle,
            distanceInfo,
            paymentDisabled,
            paymentType
        ),
        executorFactory = { OrderStoreExecutor(handle, distanceInfo, starId) },
        reducer = OrderStoreReducer
    ) {}

}