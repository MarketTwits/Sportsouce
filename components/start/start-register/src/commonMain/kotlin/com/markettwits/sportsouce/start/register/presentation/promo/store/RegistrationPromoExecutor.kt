package com.markettwits.sportsouce.start.register.presentation.promo.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.sportsouce.start.register.presentation.promo.store.RegistrationPromoStore.Intent
import com.markettwits.sportsouce.start.register.presentation.promo.store.RegistrationPromoStore.Label
import com.markettwits.sportsouce.start.register.presentation.promo.store.RegistrationPromoStore.Message
import com.markettwits.sportsouce.start.register.presentation.promo.store.RegistrationPromoStore.State
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.StartRegistrationRepository
import kotlinx.coroutines.launch

class RegistrationPromoExecutor(
    private val startId: Int,
    private val distancesId : List<Int>,
    private val repository: StartRegistrationRepository
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.Dismiss -> publish(Label.Dismiss)
            is Intent.OnClickPromo -> applyPromo(state().promo, startId)
            is Intent.OnPromoChanged -> dispatch(Message.OnPromoChanged(intent.promo))
        }
    }

    private fun applyPromo(promo: String, startId: Int) {
        scope.launch {
            dispatch(Message.ApplyPromoLoading)
            repository.getStartPromo(promo, startId,distancesId).fold(
                onSuccess = {
                    publish(Label.ApplyPromo(promo,it.discountPercent))
                    publish(Label.Dismiss)
                },
                onFailure = {
                    dispatch(Message.ApplyPromoFailed("Не удалось применить промокод"))
                }
            )
        }
    }
}
