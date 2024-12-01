package com.markettwits.start.register.presentation.registration.presentation.component.pay

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.IntentAction
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.getOrCreateKoinScope
import com.markettwits.start.register.di.startRegistrationModule
import com.markettwits.start.register.presentation.promo.component.RegistrationPromoComponentBase
import com.markettwits.start.register.presentation.registration.domain.StartRegistrationRepository
import com.markettwits.start.register.presentation.registration.presentation.components.registration.StartRegistrationStagePage
import com.markettwits.start.register.presentation.registration.presentation.components.registration.getDistancesId
import com.markettwits.start.register.presentation.registration.presentation.store.StartRegistrationPageStore.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StartPayComponentBase(
    componentContext: ComponentContext,
    innerState : StartRegistrationStagePage.Pay,
    private val outerState : State,
    private val repository: StartRegistrationRepository,
    private val intentAction: IntentAction,
    private val goBack : (StartRegistrationStagePage) -> Unit,
    private val onEventContent: (EventContent) -> Unit
) : StartPayComponent, ComponentContext by componentContext {

    private val feature = instanceKeeper.getOrCreate {
        StartPayFeature(
            repository = repository,
            currentState = outerState,
            onEventContent = onEventContent,
            intentAction = intentAction,
            onGoBack = { goBack(state.value) },
            innerState = innerState
        )
    }

    private val scope = getOrCreateKoinScope(
        listOf(startRegistrationModule)
    )

    private val slotNavigation = SlotNavigation<StartPayComponent.Config>()

    override val state: MutableStateFlow<StartRegistrationStagePage.Pay> = MutableStateFlow(innerState)

    override val newState: StateFlow<StartPayComponent.State> = feature.state

    override val childSlot: Value<ChildSlot<*, StartPayComponent.Child>> = childSlot(
        serializer = StartPayComponent.Config.serializer(),
        source = slotNavigation,
        handleBackButton = true,
        childFactory = ::childSlot
    )

    override fun onClickGoBack() {
        goBack(state.value)
    }

    override fun onClickRegistry(isWithPay: Boolean) {
        feature.registerOnStart(isWithPay)
    }

    override fun onClickPromo() {
        slotNavigation.activate(StartPayComponent.Config.StartRegistrationPromo(
            startId = outerState.registrationInfo.startId,
            distancesId = outerState.stages.getDistancesId(),
            promo = outerState.registrationInfo.promo
        ))
    }

    private fun childSlot(
        config: StartPayComponent.Config,
        componentContext: ComponentContext
    ): StartPayComponent.Child = when (config) {
        is StartPayComponent.Config.StartRegistrationPromo -> StartPayComponent.Child.StartRegistrationPromo(
            RegistrationPromoComponentBase(
                componentContext = componentContext,
                promo = config.promo,
                startId = config.startId,
                distancesId = config.distancesId,
                storeFactory = scope.get(),
                applyPromo = { string, int ->
                    onEventContent(EventContent(true,"Промокод успешно применен"))
                },
                dismiss = {
                    slotNavigation.dismiss()
                }
            )
        )
    }

}