package com.markettwits.profile.presentation.component.authorized

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.markettwits.profile.data.BaseProfileDataSource
import com.markettwits.profile.presentation.ProfileUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

class AuthorizedProfileComponent(
    context: ComponentContext,
    private val service: BaseProfileDataSource,
    private val event: (AuthorizedProfileEvent) -> Unit
) : AuthorizedProfile, ComponentContext by context {
    val scope = CoroutineScope(Dispatchers.Main)

    override val profileName: MutableValue<ProfileUiState> = MutableValue(
        stateKeeper.consume(
            key = "PROFILE_STATE",
            ProfileUiState.serializer()
        ) ?: ProfileUiState.Loading
    )

    init {
        scope.launch {
            profileName.value = service.profile()
        }
        stateKeeper.register(
            key = "PROFILE_STATE",
            ProfileUiState.serializer()
        ) { profileName.value }
    }


    override fun goEditScreen() {
        event(AuthorizedProfileEvent.EditProfile((profileName.value as ProfileUiState.Base).user))
    }

    override fun goChangePasswordScreen() {
        TODO("Not yet implemented")
    }

    override fun goMyRegistryScreen() {
        TODO("Not yet implemented")
    }

    override fun goMyMembersScreen() {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        scope.launch {
            service.exit()
            event(AuthorizedProfileEvent.SignOut)
        }
    }
}