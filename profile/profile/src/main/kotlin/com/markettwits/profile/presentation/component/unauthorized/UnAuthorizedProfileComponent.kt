package com.markettwits.profile.presentation.component.unauthorized

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.markettwits.profile.data.ProfileDataSource
import com.markettwits.profile.presentation.deprecated.ProfileUiState
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
        check()

        stateKeeper.register(
            key = "PROFILE_STATE",
            ProfileUiState.serializer()
        ) { state.value }
    }

    fun check() {
        state.value = ProfileUiState.Loading
        scope.launch {
            service.profile().fold(
                onSuccess = {
                    goAuthProfile()
                },
                onFailure = {
                    state.value = ProfileUiState.Error(it.message.toString())
                }
            )
        }
    }

    override fun signIn() {
        goSignIn()
    }
}