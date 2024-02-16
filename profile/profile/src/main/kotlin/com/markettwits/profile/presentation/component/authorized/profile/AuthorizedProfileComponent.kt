package com.markettwits.profile.presentation.component.authorized.profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.markettwits.profile.data.BaseProfileDataSource
import com.markettwits.profile.presentation.ProfileUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        event(AuthorizedProfileEvent.ChangePasswordProfile)
    }

    override fun goMyRegistryScreen() {
        event(AuthorizedProfileEvent.MyRegistries)
    }

    override fun goMyMembersScreen() {
        event(AuthorizedProfileEvent.MyMembers)
    }

    override fun signOut() {
        scope.launch {
            service.exit()
            event(AuthorizedProfileEvent.SignOut)
        }
    }
}