package com.markettwits.start.register.presentation.registration.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.start.register.presentation.registration.presentation.store.StartRegistrationPageStore.Intent
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.markettwits.start.register.presentation.registration.domain.StartRegistrationRepository
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationDistance
import com.markettwits.start.register.presentation.registration.presentation.store.StartRegistrationPageStore.Label
import com.markettwits.start.register.presentation.registration.presentation.store.StartRegistrationPageStore.State
import com.markettwits.start_cloud.model.start.fields.DistinctDistance

class StartRegistrationPageStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: StartRegistrationRepository
) {

    fun create(
        distances : List<DistinctDistance>,
        startId : Int,
        startTitle : String,
        comboId : Int?,
        paymentType: String,
        isPaymentDisabled: Boolean,
    ): StartRegistrationPageStore {
        return StartRegistrationPageStoreImpl(
            startId = startId,
            startTitle = startTitle,
            comboId = comboId,
            paymentType = paymentType,
            isPaymentDisabled = isPaymentDisabled,
            distances = distances
        )
    }

    private inner class StartRegistrationPageStoreImpl(
        private val startId : Int,
        private val startTitle : String,
        private val comboId : Int?,
        private val paymentType: String,
        private val isPaymentDisabled: Boolean,
        private val distances: List<DistinctDistance>,
    ) :
        StartRegistrationPageStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "StartRegistrationPageStore",
            initialState = StartRegistrationPageStore.State(
                stages = emptyList(),
                registrationInfo = StartRegistrationPageStore.StartRegistrationInfo(
                    startId = startId,
                    startTitle = startTitle,
                    comboId = comboId,
                    paymentType = paymentType,
                    isPaymentDisabled = isPaymentDisabled,
                )
            ),
            bootstrapper = StartRegistrationBootstrapper(repository,distances),
            executorFactory = { StartRegistrationPageExecutor() },
            reducer = StartRegistrationPageReducer
        )
}