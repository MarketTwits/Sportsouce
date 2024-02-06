package com.markettwits.start.presentation.order.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.start.data.registration.RegistrationStartRepository
import com.markettwits.start.presentation.order.store.OrderStore.Intent
import com.markettwits.start.presentation.order.store.OrderStore.Label
import com.markettwits.start.presentation.order.store.OrderStore.State

class OrderStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: RegistrationStartRepository
) {

    fun create(
        distanceInfo: DistanceItem,
        starId: Int,
        paymentDisabled: Boolean
    ): OrderStore {
        return OrderStoreImpl(distanceInfo, paymentDisabled)
    }

    private inner class OrderStoreImpl(
        private val distanceInfo: DistanceItem,
        private val paymentDisabled: Boolean
    ) :
        OrderStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "OrderStoreStore",
            initialState = State(),
            bootstrapper = OrderBootstrapper(repository, distanceInfo, paymentDisabled),
            executorFactory = { OrderStoreExecutor(repository) },
            reducer = OrderStoreReducer
        )
}