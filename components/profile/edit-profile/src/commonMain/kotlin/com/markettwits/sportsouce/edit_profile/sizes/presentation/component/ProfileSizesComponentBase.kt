package com.markettwits.sportsouce.edit_profile.sizes.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.edit_profile.sizes.presentation.store.ProfileSizesStore
import com.markettwits.sportsouce.edit_profile.sizes.presentation.store.ProfileSizesStoreFactory
import kotlinx.coroutines.*

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileSizesComponentBase(
    componentContext: ComponentContext,
    storeFactory: ProfileSizesStoreFactory,
    private val pop: () -> Unit,
) : ProfileSizesComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    override val state = store.stateFlow
    override fun obtainEvent(intent: ProfileSizesStore.Intent) = store.accept(intent)

    init {
        val back = BackCallback { store.accept(ProfileSizesStore.Intent.GoBack) }
        backHandler.register(back)
        lifecycle.doOnDestroy { backHandler.unregister(back); scope.cancel() }
        scope.launch { store.labels.collect { if (it == ProfileSizesStore.Label.GoBack) pop() } }
    }
}
