package com.markettwits.sportsouce.start.register.presentation.registration.pay.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.IntentAction
import com.markettwits.core.errors.api.throwable.isNetworkConnectionError
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.crashlitics.api.logging.ExceptionLoggingTracker
import com.markettwits.crashlitics.api.logging.errorShaker
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.StartRegistrationRepository
import com.markettwits.sportsouce.start.register.presentation.registration.pay.store.StartPayStore.Intent
import com.markettwits.sportsouce.start.register.presentation.registration.pay.store.StartPayStore.Label
import com.markettwits.sportsouce.start.register.presentation.registration.pay.store.StartPayStore.Message
import com.markettwits.sportsouce.start.register.presentation.registration.pay.store.StartPayStore.State
import com.markettwits.sportsouce.start.register.presentation.registration.registration.components.StartRegistrationStagePage
import kotlinx.coroutines.launch


abstract class StartPayExecutorHandler(
    private val repository: StartRegistrationRepository,
    private val intentAction: IntentAction,
    override val exceptionTracker: ExceptionTracker,
) : CoroutineExecutor<Intent, Unit, State, Message, Label>(), ExceptionLoggingTracker {

    override val tag: String = "StartPayExecutor"

    fun obtainPromoSuccess(
        promo : String
    ){
        dispatch(Message.UpdatePromoSuccess(promo))
        publish(Label.SendEvent(EventContent(true, PROMO_APPLY_SUCCESS)))
    }

    fun obtainStartRegistrationResult(
        statePage : StartRegistrationStagePage.Pay,
        isPayOnline : Boolean,
    ) {
        scope.launch {
            dispatch(Message.UpdatePriceLoadingStarted)
            repository.registerOnStart(
                comboId = statePage.startInfo.comboId,
                startId = statePage.startInfo.startId,
                promo = statePage.startInfo.promo,
                registrationWithoutPayment = !isPayOnline,
                distances = statePage.distances,
            ).fold(onSuccess = {
                if (it.isSuccess) {
                    if (it.paymentUrl.isNotEmpty()) {
                        intentAction.openWebPage(it.paymentUrl)
                    } else {
                        publish(Label.SendEvent(EventContent(true, REGISTRATION_SUCCESS_MESSAGE)))
                    }
                    publish(Label.GoSuccess)
                }
                dispatch(Message.UpdatePriceLoadingFinished)
            }, onFailure = {
                publish(Label.SendEvent(EventContent(false, it.mapToSauceError().mapToString())))
                dispatch(Message.UpdatePriceLoadingFinished)
                if (!it.isNetworkConnectionError())
                    errorShaker(it) { "Failed when try create registration on ${statePage.startInfo.startId} " }
            })
        }

    }

    fun obtainStartPriceResult(
        startRegistrationStage : StartRegistrationStagePage.Pay
    ){
        scope.launch {
            dispatch(Message.UpdatePriceLoadingStarted)
            repository.getStartPrice(
                comboId = startRegistrationStage.startInfo.comboId,
                startId = startRegistrationStage.startInfo.startId,
                promo = startRegistrationStage.startInfo.promo,
                distances = startRegistrationStage.distances,
            ).fold(onSuccess = {
                dispatch(Message.UpdatePrice(it))
                dispatch(Message.UpdatePriceLoadingFinished)
            }) {
                val message = it.mapToSauceError().mapToString()
                publish(Label.SendEvent(EventContent(false, message)))
                dispatch(Message.UpdatePriceLoadingFinished)

                if (!it.isNetworkConnectionError())
                    errorShaker(it) { "Failed when try get start price $startRegistrationStage" }
                publish(Label.GoBack)
            }
        }
    }


    private companion object {
        const val REGISTRATION_SUCCESS_MESSAGE = "Вы успешно зарегестрировались на старт"
        const val PROMO_APPLY_SUCCESS = "Промокод успешно применен"
    }

}