package com.markettwits.start.presentation.promo.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.start.data.registration.RegistrationStartRepository
import com.markettwits.start.presentation.promo.store.RegistrationPromoStore.Intent
import com.markettwits.start.presentation.promo.store.RegistrationPromoStore.Label
import com.markettwits.start.presentation.promo.store.RegistrationPromoStore.State

class RegistrationPromoStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: RegistrationStartRepository,
) {

    fun create(startId: Int, promo: String): RegistrationPromoStore =
        RegistrationPromoStoreImpl(startId, promo)

    private inner class RegistrationPromoStoreImpl(
        private val startId: Int,
        private val promo: String
    ) :
        RegistrationPromoStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "RegistrationPromoStore",
            initialState = State(promo = promo),
            executorFactory = { RegistrationPromoExecutor(startId, repository) },
            reducer = RegistrationPromoReducer
        )
}