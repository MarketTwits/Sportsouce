package com.markettwits.start.register.presentation.promo.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.start.register.presentation.promo.store.RegistrationPromoStore.Intent
import com.markettwits.start.register.presentation.promo.store.RegistrationPromoStore.Label
import com.markettwits.start.register.presentation.promo.store.RegistrationPromoStore.State
import com.markettwits.start.register.presentation.registration.common.domain.StartRegistrationRepository

class RegistrationPromoStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: StartRegistrationRepository,
) {

    fun create(
        startId: Int,
        distancesId : List<Int>,
        promo: String,
    ): RegistrationPromoStore =
        RegistrationPromoStoreImpl(startId,distancesId, promo)

    private inner class RegistrationPromoStoreImpl(
        private val startId: Int,
        private val distancesId: List<Int>,
        private val promo: String
    ) :
        RegistrationPromoStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "RegistrationPromoStore",
            initialState = State(promo = promo),
            executorFactory = { RegistrationPromoExecutor(startId, distancesId, repository) },
            reducer = RegistrationPromoReducer
        )
}