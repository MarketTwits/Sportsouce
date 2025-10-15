package com.markettwits.sportsouce.club.subscription.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarComponentHandler
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarVisibilityStrategy
import com.markettwits.sportsouce.club.subscription.presentation.store.SubscriptionPricingStore
import com.markettwits.sportsouce.club.subscription.presentation.store.SubscriptionPricingStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SubscriptionPricingComponentBase(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    clubRepository: com.markettwits.sportsouce.club.common.domain.ClubRepository,
    private val output: (SubscriptionPricingComponent.Output) -> Unit,
) : SubscriptionPricingComponent, BottomBarComponentHandler(), ComponentContext by componentContext {

    init {
        subscribeOnBottomBar(BottomBarVisibilityStrategy.AlwaysInvisible)
    }

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val store = instanceKeeper.getStore {
        SubscriptionPricingStoreFactory(
            storeFactory = storeFactory,
            clubRepository = clubRepository
        ).create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<SubscriptionPricingStore.State> = store.stateFlow

    override fun obtainEvent(intent: SubscriptionPricingStore.Intent) {
        store.accept(intent)
    }

    init {
        store.labels.onEach { label ->
            when (label) {
                is SubscriptionPricingStore.Label.Dismiss -> {
                    output(SubscriptionPricingComponent.Output.Dismiss)
                }

                is SubscriptionPricingStore.Label.Registration -> {
                    output(SubscriptionPricingComponent.Output.Registration(label.type))
                }
            }
        }.launchIn(scope)
    }
}