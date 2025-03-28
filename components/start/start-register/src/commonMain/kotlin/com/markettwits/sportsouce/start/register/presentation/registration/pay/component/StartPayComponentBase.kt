package com.markettwits.sportsouce.start.register.presentation.registration.pay.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.getOrCreateKoinScope
import com.markettwits.sportsouce.start.register.di.startRegistrationModule
import com.markettwits.sportsouce.start.register.presentation.promo.component.RegistrationPromoComponentBase
import com.markettwits.sportsouce.start.register.presentation.registration.pay.store.StartPayStore
import com.markettwits.sportsouce.start.register.presentation.registration.pay.store.StartPayStoreFactory
import com.markettwits.sportsouce.start.register.presentation.registration.registration.components.StartRegistrationStagePage
import com.markettwits.sportsouce.start.register.presentation.registration.registration.components.getDistancesId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class StartPayComponentBase(
    componentContext: ComponentContext,
    innerState : StartRegistrationStagePage.Pay,
    private val storeFactory: StartPayStoreFactory,
    private val goBack : (StartRegistrationStagePage) -> Unit,
    private val goSuccess : () -> Unit,
    private val onEventContent: (EventContent) -> Unit
) : StartPayComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        storeFactory.create(innerState)
    }

    private val scope = getOrCreateKoinScope(
        listOf(startRegistrationModule)
    )

    private val slotNavigation = SlotNavigation<StartPayComponent.Config>()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val newState: StateFlow<StartPayStore.State>  = store.stateFlow

    override val childSlot: Value<ChildSlot<*, StartPayComponent.Child>> = childSlot(
        serializer = StartPayComponent.Config.serializer(),
        source = slotNavigation,
        handleBackButton = true,
        childFactory = ::childSlot
    )

    override fun onClickGoBack() {
        store.accept(StartPayStore.Intent.OnClickGoBack)
    }

    override fun onClickRegistry(isWithPay: Boolean) {
        if (isWithPay)
            store.accept(StartPayStore.Intent.OnClickRegistryWithPay)
        else
            store.accept(StartPayStore.Intent.OnClickRegistryWithoutPay)
    }

    override fun onClickPromo() {
        store.accept(StartPayStore.Intent.OnClickPromo)
    }

    override val value: StartRegistrationStagePage = innerState

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
                    store.accept(StartPayStore.Intent.UpdatePromo(string))
                },
                dismiss = {
                    slotNavigation.dismiss()
                }
            )
        )
    }

    init {
        store.labels.onEach {
            when(it){
                is StartPayStore.Label.GoBack -> goBack(store.state.state)
                is StartPayStore.Label.GoPromo -> {
                    slotNavigation.activate(
                        StartPayComponent.Config.StartRegistrationPromo(
                            startId = store.state.state.startInfo.startId,
                            distancesId = store.state.state.distances.getDistancesId(),
                            promo = store.state.state.startInfo.promo
                        )
                    )
                }
                is StartPayStore.Label.SendEvent -> {
                    onEventContent(it.eventContent)
                }

                is StartPayStore.Label.GoSuccess -> goSuccess()
            }
        }.launchIn(CoroutineScope(Dispatchers.Main.immediate))
    }

}