package com.markettwits.sportsouce.start.register.presentation.registration.pay.store

import com.markettwits.IntentAction
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.StartRegistrationRepository
import com.markettwits.sportsouce.start.register.presentation.registration.pay.store.StartPayStore.Intent
import com.markettwits.sportsouce.start.register.presentation.registration.pay.store.StartPayStore.Label

class StartPayExecutor(
    repository: StartRegistrationRepository,
    intentAction: IntentAction,
    override val exceptionTracker: ExceptionTracker
) : StartPayExecutorHandler(repository, intentAction, exceptionTracker) {

    override fun executeIntent(intent: Intent) {
        when (intent) {

            is Intent.OnClickGoBack -> publish(Label.GoBack)

            is Intent.OnClickPromo -> publish(Label.GoPromo(state().state.startInfo.promo))

            is Intent.OnClickRegistryWithPay -> {
                obtainStartRegistrationResult(
                    statePage = state().state,
                    isPayOnline = true
                )
            }

            is Intent.OnClickRegistryWithoutPay -> {
                obtainStartRegistrationResult(
                    statePage = state().state,
                    isPayOnline = false
                )
            }

            is Intent.UpdatePromo -> {
                obtainPromoSuccess(intent.promo)
                obtainStartPriceResult(state().state)
            }
        }
    }

    override fun executeAction(action: Unit) {
        obtainStartPriceResult(state().state)
    }
}