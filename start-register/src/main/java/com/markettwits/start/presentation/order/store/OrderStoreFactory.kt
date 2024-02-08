package com.markettwits.start.presentation.order.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.start.presentation.order.domain.interactor.OrderInteractor
import com.markettwits.start.presentation.order.store.OrderStore.Intent
import com.markettwits.start.presentation.order.store.OrderStore.Label
import com.markettwits.start.presentation.order.store.OrderStore.State

class OrderStoreFactory(
    private val storeFactory: StoreFactory,
    private val interactor: OrderInteractor,
    private val handle: OrderStoreExecutorHandle
) {
    fun create(
        state: State,
        distanceInfo: DistanceItem,
        starId: Int,
        paymentType: String,
        paymentDisabled: Boolean
    ): OrderStore = object : OrderStore, Store<Intent, State, Label> by storeFactory.create(
        name = "OrderStoreStore",
        initialState = state,
        bootstrapper = OrderBootstrapper(
            state.orderStatement == null,
            interactor,
            distanceInfo,
            paymentDisabled,
            paymentType
        ),
        executorFactory = { OrderStoreExecutor(handle, distanceInfo, starId) },
        reducer = OrderStoreReducer
    ) {}

}