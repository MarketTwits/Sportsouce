package com.markettwits.start.register.presentation.promo.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.start.register.presentation.promo.store.RegistrationPromoStore.Intent
import com.markettwits.start.register.presentation.promo.store.RegistrationPromoStore.Label
import com.markettwits.start.register.presentation.promo.store.RegistrationPromoStore.Message
import com.markettwits.start.register.presentation.promo.store.RegistrationPromoStore.State
import kotlinx.coroutines.launch

class RegistrationPromoExecutor(
    private val startId: Int,
    private val distancesId : List<Int>,
    private val repository: com.markettwits.start.register.data.registration.RegistrationStartRepository
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.Dismiss -> publish(Label.Dismiss)
            is Intent.OnClickPromo -> applyPromo(getState().promo, startId)
            is Intent.OnPromoChanged -> dispatch(Message.OnPromoChanged(intent.promo))
        }
    }

    private fun applyPromo(promo: String, startId: Int) {
        scope.launch {
            dispatch(Message.ApplyPromoLoading)
            repository.promo(promo, startId,distancesId).fold(
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
