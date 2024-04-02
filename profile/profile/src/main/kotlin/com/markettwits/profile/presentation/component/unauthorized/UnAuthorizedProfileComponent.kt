package com.markettwits.profile.presentation.component.unauthorized

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.doOnStart
import com.markettwits.profile.data.ProfileDataSource
import com.markettwits.profile.presentation.component.base.ProfileUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UnAuthorizedProfileComponent(
    context: ComponentContext,
    private val service: ProfileDataSource,
    private val goAuthProfile: () -> Unit,
    private val goSignIn: () -> Unit
) : UnAuthorizedProfile, ComponentContext by context {
    val scope = CoroutineScope(Dispatchers.Main.immediate)

    override val state: MutableValue<ProfileUiState> = MutableValue(
        stateKeeper.consume(
            key = "PROFILE_STATE",
            ProfileUiState.serializer()
        ) ?: ProfileUiState.Loading
    )

    init {
        lifecycle.doOnStart {
            check()
        }

        stateKeeper.register(
            key = "PROFILE_STATE",
            ProfileUiState.serializer()
        ) { state.value }
    }

    private fun check() {
        state.value = ProfileUiState.Loading
        scope.launch {
            service.profile().fold(
                onSuccess = {
                    goAuthProfile()
                },
                onFailure = {
                    state.value = ProfileUiState.Error(it.message.toString())
                    Log.e("mt05", it.stackTraceToString())
                }
            )
        }
    }

    override fun signIn() {
        goSignIn()
    }
}