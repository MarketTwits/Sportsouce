package com.markettwits.profile.presentation

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.profile.data.AuthDataSource
import com.markettwits.profile.data.ProfileDataSource
import com.markettwits.profile.presentation.sign_in.SignInFieldUiState
import com.markettwits.profile.presentation.sign_in.SignInUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileInstanceKeeper(
    private val service: ProfileDataSource,
    private val goToAuth: () -> Unit
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(Dispatchers.Main)
    val state: MutableValue<ProfileUiState> = MutableValue(ProfileUiState.Initial)

    fun init() {
        scope.launch {
            state.value = service.profile()
        }
    }

    fun exit() {
        scope.launch {
            service.exit()
            openAuthScreen()
        }
    }

    fun openAuthScreen() {
        goToAuth()
    }
}