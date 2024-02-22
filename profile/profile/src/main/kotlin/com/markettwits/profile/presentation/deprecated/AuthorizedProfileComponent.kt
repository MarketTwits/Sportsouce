package com.markettwits.profile.presentation.deprecated

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.markettwits.profile.domain.UserUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthorizedProfileComponent(
    context: ComponentContext,
    private val useCase: UserUseCase,
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
            profileName.value = ProfileUiState.Loading
            useCase.user().fold(onSuccess = {
                    profileName.value = ProfileUiState.Base(it)
                }, onFailure = {
                    event(AuthorizedProfileEvent.SignOut)
                })
            //  profileName.value = service.profile()
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
            // service.exit()
            event(AuthorizedProfileEvent.SignOut)
        }
    }
}