package com.markettwits.start.register.presentation.registration.presentation.component.pay.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.IntentAction
import com.markettwits.core.errors.api.throwable.isNetworkConnectionError
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.start.register.presentation.registration.domain.StartRegistrationRepository
import com.markettwits.start.register.presentation.registration.presentation.component.pay.store.StartPayStore.Intent
import com.markettwits.start.register.presentation.registration.presentation.component.pay.store.StartPayStore.Label
import com.markettwits.start.register.presentation.registration.presentation.component.pay.store.StartPayStore.State
import com.markettwits.start.register.presentation.registration.presentation.component.pay.store.StartPayStore.Message
import com.markettwits.start.register.presentation.registration.presentation.components.registration.StartRegistrationStagePage
import kotlinx.coroutines.launch

class StartPayExecutor(
    repository: StartRegistrationRepository,
    intentAction: IntentAction,
    override val exceptionTracker: ExceptionTracker
) : StartPayExecutorHandler(repository, intentAction, exceptionTracker) {

    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {

            is Intent.OnClickGoBack -> publish(Label.GoBack)

            is Intent.OnClickPromo -> publish(Label.GoPromo(getState().state.startInfo.promo))

            is Intent.OnClickRegistryWithPay -> {
                obtainStartRegistrationResult(
                    statePage = getState().state,
                    isPayOnline = true
                )
            }

            is Intent.OnClickRegistryWithoutPay -> {
                obtainStartRegistrationResult(
                    statePage = getState().state,
                    isPayOnline = false
                )
            }

            is Intent.UpdatePromo -> {
                obtainPromoSuccess(intent.promo)
                obtainStartPriceResult(getState().state)
            }
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        obtainStartPriceResult(getState().state)
    }
}