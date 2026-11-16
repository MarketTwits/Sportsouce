package com.markettwits.sportsouce.notification.impl.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.auth.flow.api.root.RootAuthFlowComponent
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.essenty.lifecycle.*
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.*
import com.markettwits.sportsouce.auth.flow.api.root.RootAuthFlowComponentBase
import com.markettwits.sportsouce.notification.api.NotificationComponent
import com.markettwits.sportsouce.notification.impl.presentation.store.*
import com.markettwits.sportsouce.notification.api.store.NotificationStore
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable

@OptIn(ExperimentalCoroutinesApi::class)
internal class NotificationComponentBase(
    context: ComponentContext,
    factory: NotificationStoreFactory,
    onBack: () -> Unit,
    onOpenStart: (Int) -> Unit,
) : NotificationComponent, ComponentContext by context {
    private val store = instanceKeeper.getStore { factory.create() }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val navigation = SlotNavigation<Auth>()
    override val authSlot: Value<ChildSlot<*, RootAuthFlowComponent>> = childSlot(source = navigation, serializer = Auth.serializer(), handleBackButton = true) { _, child ->
        RootAuthFlowComponentBase(child, goBack = { navigation.dismiss() }, goProfile = {
            navigation.dismiss()
            store.accept(NotificationStore.Intent.Resume)
        })
    }
    @Serializable private data object Auth

    init {
        lifecycle.doOnResume { store.accept(NotificationStore.Intent.Resume) }
        lifecycle.doOnDestroy { scope.cancel() }
        scope.launch {
            store.labels.collect { label -> when (label) {
                NotificationStore.Label.Back -> onBack()
                NotificationStore.Label.SignIn -> navigation.activate(Auth)
                is NotificationStore.Label.OpenStart -> onOpenStart(label.id)
            } }
        }
    }

    override val state = store.stateFlow
    override fun obtainEvent(intent: NotificationStore.Intent) = store.accept(intent)
}
