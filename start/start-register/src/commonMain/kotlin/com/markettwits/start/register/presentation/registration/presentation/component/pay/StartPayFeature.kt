package com.markettwits.start.register.presentation.registration.presentation.component.pay

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.IntentAction
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.start.register.presentation.registration.domain.StartRegistrationRepository
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationDistance
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationInfo
import com.markettwits.start.register.presentation.registration.presentation.components.registration.StartRegistrationStagePage
import com.markettwits.start.register.presentation.registration.presentation.components.registration.getRegistrationsStage
import com.markettwits.start.register.presentation.registration.presentation.store.StartRegistrationPageStore.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StartPayFeature(
    private val repository: StartRegistrationRepository,
    private val intentAction: IntentAction,
    private val onEventContent: (EventContent) -> Unit,
    private val onGoBack : () -> Unit,
    innerState : StartRegistrationStagePage.Pay
) : InstanceKeeper.Instance {

    private val scope = CoroutineScope(Dispatchers.Main)

    val state : MutableStateFlow<StartPayComponent.State> = MutableStateFlow(
        StartPayComponent.State(
            isLoading = false,
            state = innerState
        )
    )

    init {
        getPrice()
    }

    fun updatePromo(
        value : String
    ){
        scope.launch {
            state.emit(
                state.value.copy(
                    state = state.value.state.copy(
                        startInfo = state.value.state.startInfo.copy(
                            promo = value
                        )
                    )
                )
            )
            onEventContent(EventContent(true,"Промокод успешно применен"))
            getPrice()
        }
    }

    fun registerOnStart(isPayOnline : Boolean,){
        scope.launch {
            state.emit(state.value.copy(isLoading = true))
            repository.registerOnStart(
                comboId = state.value.state.startInfo.comboId,
                startId = state.value.state.startInfo.startId,
                promo = state.value.state.startInfo.promo,
                registrationWithoutPayment = !isPayOnline,
                distances = state.value.state.distances,
            ).fold(onSuccess = {
                if (it.isSuccess){
                    if (it.paymentUrl.isNotEmpty()) {
                        state.emit(state.value.copy(isLoading = false))
                        intentAction.openWebPage(it.paymentUrl)
                    } else {
                        state.emit(state.value.copy(isLoading = false))
                        onEventContent(EventContent(true, "Вы успешно зарегестрировались на старт"))
                    }
                }else{
                    state.emit(state.value.copy(isLoading = false))
                    onEventContent(EventContent(false,it.message))
                }
            }, onFailure = {
                val message = it.mapToSauceError().mapToString()
                onEventContent(EventContent(false,message))
                state.emit(state.value.copy(isLoading = false))
            })
        }
    }

    private fun getPrice(){
        scope.launch {
            state.emit(state.value.copy(isLoading = true))
            repository.getStartPrice(
                comboId = state.value.state.startInfo.comboId,
                startId = state.value.state.startInfo.startId,
                promo = state.value.state.startInfo.promo,
                distances = state.value.state.distances,
            ).fold(onSuccess = {
                val payStage = state.value.copy(
                    state = state.value.state.copy(price = it)
                )
                state.emit(payStage)
                state.emit(state.value.copy(isLoading = false))
            }, onFailure = {
                val message = it.mapToSauceError().mapToString()
                onEventContent(EventContent(false,message))
                state.emit(state.value.copy(isLoading = false))
                onGoBack()
            })
        }
    }

}