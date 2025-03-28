package com.markettwits.sportsouce.start.register.presentation.registration.registration.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.StartRegistrationRepository
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