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
    val scope = CoroutineScope(Dispatchers.Main)

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
            when(service.profile()){
                is ProfileUiState.Base ->  goAuthProfile()
                is ProfileUiState.Error -> state.value = ProfileUiState.Error("")
                is ProfileUiState.Loading -> state.value = ProfileUiState.Loading
            }
        }
    }

    override fun signIn() {
        goSignIn()
    }
}