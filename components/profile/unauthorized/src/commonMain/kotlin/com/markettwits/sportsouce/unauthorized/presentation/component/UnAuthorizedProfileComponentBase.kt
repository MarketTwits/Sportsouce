package com.markettwits.sportsouce.unauthorized.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.doOnResume
import com.markettwits.sportsouce.unauthorized.domain.UnauthorizedProfileUseCase
import com.markettwits.sportsouce.unauthorized.presentation.components.UnAuthorizedProfileUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UnAuthorizedProfileComponentBase(
    context: ComponentContext,
    private val useCase: UnauthorizedProfileUseCase,
    private val goAuthProfile: () -> Unit,
    private val goSignIn: () -> Unit
) : UnAuthorizedProfileComponent, ComponentContext by context {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    override val state: MutableValue<UnAuthorizedProfileUiState> = MutableValue(
        stateKeeper.consume(
            key = "PROFILE_STATE",
            UnAuthorizedProfileUiState.serializer()
        ) ?: UnAuthorizedProfileUiState.Loading
    )


    init {
        lifecycle.doOnResume {
            check()
        }
        stateKeeper.register(
            key = "PROFILE_STATE",
            UnAuthorizedProfileUiState.serializer()
        ) { state.value }
    }

    private fun check() {
        state.value = UnAuthorizedProfileUiState.Loading
        scope.launch {
            useCase.authorize().fold(
                onSuccess = {
                    goAuthProfile()
                },
                onFailure = {
                    state.value = UnAuthorizedProfileUiState.Error(it.message.toString())
                }
            )
        }
    }

    override fun signIn() {
        goSignIn()
    }
}