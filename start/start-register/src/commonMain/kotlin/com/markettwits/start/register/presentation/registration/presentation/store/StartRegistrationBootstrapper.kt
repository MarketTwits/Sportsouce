package com.markettwits.start.register.presentation.registration.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.start.register.presentation.registration.domain.StartRegistrationRepository
import com.markettwits.start_cloud.model.start.fields.DistinctDistance
import kotlinx.coroutines.launch

class StartRegistrationBootstrapper(
    private val repository: StartRegistrationRepository,
    private val distinctDistances: List<DistinctDistance>
) : CoroutineBootstrapper<StartRegistrationPageStore.Action>() {
    override fun invoke() {
        scope.launch {
            dispatch(StartRegistrationPageStore.Action.Loading)
            repository.isRegisterAvailable()
                .onSuccess {
                    repository.getStartDistances(distinctDistances).fold(
                        onSuccess = {
                            dispatch(StartRegistrationPageStore.Action.Loaded(it))
                        }, onFailure = {
                            dispatch(StartRegistrationPageStore.Action.Failed(it.mapToSauceError()))
                        }
                    )
                }
                .onFailure {
                    dispatch(StartRegistrationPageStore.Action.Unauthorized)
                }
        }
    }
}