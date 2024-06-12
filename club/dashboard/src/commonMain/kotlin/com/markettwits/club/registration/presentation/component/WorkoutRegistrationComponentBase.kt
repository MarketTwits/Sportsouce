package com.markettwits.club.registration.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.club.registration.domain.RegistrationType
import com.markettwits.club.registration.domain.WorkoutRegistrationForm
import com.markettwits.club.registration.presentation.store.store.WorkoutRegistrationStore
import com.markettwits.club.registration.presentation.store.store.WorkoutRegistrationStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class WorkoutRegistrationComponentBase(
    componentContext: ComponentContext,
    storeFactory: WorkoutRegistrationStoreFactory,
    private val type: RegistrationType,
    private val output: (WorkoutRegistrationComponent.Output) -> Unit

) : WorkoutRegistrationComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val store = instanceKeeper.getStore {
        storeFactory.create(type)
    }
    override val state: StateFlow<WorkoutRegistrationStore.State> = store.stateFlow

    override fun onFormChanged(registrationForm: WorkoutRegistrationForm) {
        store.accept(WorkoutRegistrationStore.Intent.OnValueChanged(registrationForm))
    }

    override fun onClickRegistration() {
        store.accept(WorkoutRegistrationStore.Intent.OnClickRegistration)
    }

    override fun onClickPhone(phone: String) {
        store.accept(WorkoutRegistrationStore.Intent.OnClickPhone(phone))
    }

    override fun onClickLink(link: String) {
        store.accept(WorkoutRegistrationStore.Intent.OnClickUrl(link))
    }

    override fun onClickDismiss() {
        store.accept(WorkoutRegistrationStore.Intent.Dismiss)
    }

    override fun onClickFinish() {
        store.accept(WorkoutRegistrationStore.Intent.OnClickContinueAfterSuccess)
    }

    init {
        store.labels.onEach {
            when (it) {
                is WorkoutRegistrationStore.Label.Dismiss -> output(WorkoutRegistrationComponent.Output.Dismiss)
            }
        }.launchIn(scope)
    }
}
