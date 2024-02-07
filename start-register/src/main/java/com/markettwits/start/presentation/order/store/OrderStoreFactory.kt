package com.markettwits.start.presentation.order.store

import android.util.Log
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.start.data.registration.RegistrationStartRepository
import com.markettwits.start.presentation.order.domain.interactor.OrderInteractor
import com.markettwits.start.presentation.order.store.OrderStore.Intent
import com.markettwits.start.presentation.order.store.OrderStore.Label
import com.markettwits.start.presentation.order.store.OrderStore.State
import kotlinx.serialization.Serializable

class OrderStoreFactory(
    private val storeFactory: StoreFactory,
    private val interactor: OrderInteractor
) {

    //    fun create(
//        stateKeeper: StateKeeper,
//        distanceInfo: DistanceItem,
//        starId: Int,
//        paymentType: String,
//        paymentDisabled: Boolean
//    ): OrderStore {
//        return OrderStoreImpl(stateKeeper,distanceInfo, paymentDisabled, paymentType).also {
//            stateKeeper.register(key = ORDER_STORE_STATE_KEY, strategy = State.serializer()) {
//                it.state
//            }
//            Log.e("mt05", it.state.toString())
//        }
//    }
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
        executorFactory = { OrderStoreExecutor(interactor) },
        reducer = OrderStoreReducer
    ) {}

    private inner class OrderStoreImpl(
        private val currentState: State,
        private val stateKeeper: StateKeeper,
        private val distanceInfo: DistanceItem,
        private val paymentDisabled: Boolean,
        private val paymentType: String
    ) : OrderStore, Store<Intent, State, Label> by storeFactory.create(
        name = "OrderStoreStore",
        initialState = stateKeeper.consume(
            key = ORDER_STORE_STATE_KEY,
            strategy = State.serializer()
        ) ?: State(),
        bootstrapper = OrderBootstrapper(
            currentState.orderStatement == null,
            interactor,
            distanceInfo,
            paymentDisabled,
            paymentType
        ),
        executorFactory = { OrderStoreExecutor(interactor) },
        reducer = OrderStoreReducer
    )

    //        .also({
//        stateKeeper.register(key = ORDER_STORE_STATE_KEY, strategy = State.serializer()) {
//            it.state
//        }
//    })
    private companion object {
        const val ORDER_STORE_STATE_KEY = "ORDER_STORE_STATE_KEY"
    }
}