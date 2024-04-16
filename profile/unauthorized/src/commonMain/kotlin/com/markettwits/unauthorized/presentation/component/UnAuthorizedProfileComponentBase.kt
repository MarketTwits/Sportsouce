package com.markettwits.unauthorized.presentation.component

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.doOnStart
import com.markettwits.unauthorized.domain.UnauthorizedProfileUseCase
import com.markettwits.unauthorized.presentation.components.UnAuthorizedProfileUiState
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
        lifecycle.doOnStart {
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
                    Log.e("mt05", it.stackTraceToString())
                }
            )
        }
    }

    override fun signIn() {
        goSignIn()
    }
}