package com.markettwits.sportsouce.unauthorized.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.doOnResume
import com.markettwits.sportsouce.unauthorized.domain.UnauthorizedProfileUseCase
import com.markettwits.sportsouce.unauthorized.domain.GuestWelcomeRepository
import com.markettwits.sportsouce.unauthorized.domain.LocalGuestWelcomeRepository
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.cancel
import com.markettwits.sportsouce.unauthorized.presentation.components.UnAuthorizedProfileUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.serializer

class UnAuthorizedProfileComponentBase(
    context: ComponentContext,
    private val useCase: UnauthorizedProfileUseCase,
    private val goAuthProfile: () -> Unit,
    private val goSignIn: () -> Unit,
    private val welcomeRepository: GuestWelcomeRepository = LocalGuestWelcomeRepository,
) : UnAuthorizedProfileComponent, ComponentContext by context {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    override val state: MutableValue<UnAuthorizedProfileUiState> = MutableValue(
        stateKeeper.consume(
            key = "PROFILE_STATE",
            UnAuthorizedProfileUiState.serializer()
        ) ?: UnAuthorizedProfileUiState.Loading
    )


    private var welcomeIntroConsumed = stateKeeper.consume("WELCOME_INTRO", Boolean.serializer()) ?: false

    init {
        lifecycle.doOnDestroy { scope.cancel() }
        stateKeeper.register("WELCOME_INTRO", Boolean.serializer()) { welcomeIntroConsumed }
        lifecycle.doOnResume {
            check()
        }
        stateKeeper.register(
            key = "PROFILE_STATE",
            UnAuthorizedProfileUiState.serializer()
        ) { state.value }
    }

    private fun check() {
        // Keep the welcome photograph mounted while rechecking on return from the auth stack.
        if (state.value !is UnAuthorizedProfileUiState.Error) state.value = UnAuthorizedProfileUiState.Loading
        scope.launch {
            useCase.authorize().fold(
                onSuccess = {
                    goAuthProfile()
                },
                onFailure = {
                    if (state.value !is UnAuthorizedProfileUiState.Error) {
                        state.value = UnAuthorizedProfileUiState.Error(it.message.toString(), welcomeRepository.cachedContent())
                    }
                    // Refresh in the background; the current content and its animation stay stable.
                    scope.launch { welcomeRepository.refresh() }
                }
            )
        }
    }

    override fun consumeWelcomeIntro(): Boolean {
        val playIntro = !welcomeIntroConsumed
        welcomeIntroConsumed = true
        return playIntro
    }

    override fun signIn() {
        goSignIn()
    }
}
