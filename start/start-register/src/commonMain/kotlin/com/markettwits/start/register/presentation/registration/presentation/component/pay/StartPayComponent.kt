package com.markettwits.start.register.presentation.registration.presentation.component.pay

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.markettwits.start.register.presentation.promo.component.RegistrationPromoComponent
import com.markettwits.start.register.presentation.registration.presentation.component.StartStageComponent
import com.markettwits.start.register.presentation.registration.presentation.components.registration.StartRegistrationStagePage
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface StartPayComponent : StartStageComponent {

        override val state: StateFlow<StartRegistrationStagePage.Pay>

        val newState : StateFlow<State>

        val childSlot: Value<ChildSlot<*, Child>>

        fun onClickGoBack()

        fun onClickRegistry(isWithPay : Boolean)

        fun onClickPromo()


        data class State(
            val isLoading : Boolean,
            val state : StartRegistrationStagePage.Pay
        )

        @Serializable
        sealed interface Config {

            @Serializable
            data class StartRegistrationPromo(
                val startId: Int,
                val distancesId: List<Int>,
                val promo: String
            ) : Config
        }

        sealed class Child {
            data class StartRegistrationPromo(val component: RegistrationPromoComponent) : Child()
        }
    }