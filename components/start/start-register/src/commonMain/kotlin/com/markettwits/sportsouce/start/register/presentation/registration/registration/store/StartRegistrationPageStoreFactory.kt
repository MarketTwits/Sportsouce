package com.markettwits.sportsouce.start.register.presentation.registration.registration.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.start.register.presentation.registration.registration.store.StartRegistrationPageStore.Intent
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationInfo
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.StartRegistrationRepository
import com.markettwits.sportsouce.start.register.presentation.registration.registration.store.StartRegistrationPageStore.Label
import com.markettwits.sportsouce.start.register.presentation.registration.registration.store.StartRegistrationPageStore.State
import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance

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
            initialState = State(
                stages = emptyList(),
                registrationInfo = StartRegistrationInfo(
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