package com.markettwits.sportsouce.profile

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher
import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User
import com.markettwits.sportsouce.unauthorized.domain.UnauthorizedProfileUseCase
import com.markettwits.sportsouce.unauthorized.presentation.component.UnAuthorizedProfileComponentBase
import com.markettwits.sportsouce.unauthorized.presentation.components.UnAuthorizedProfileUiState
import kotlin.test.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class GuestWelcomeLifecycleTest {
    @BeforeTest fun setMain() { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    @AfterTest fun resetMain() { Dispatchers.resetMain() }
    private fun component(keeper: StateKeeperDispatcher) = UnAuthorizedProfileComponentBase(
        context = DefaultComponentContext(LifecycleRegistry(), stateKeeper = keeper),
        useCase = object : UnauthorizedProfileUseCase {
            override suspend fun authorize(): Result<User> = error("Not resumed in this test")
            override suspend fun exit() = Unit
        },
        goAuthProfile = {}, goSignIn = {},
    )

    @Test fun returningFromSignInOrRecheckingAuthorizationDoesNotReplayTheIntro() {
        val component = component(StateKeeperDispatcher())
        assertTrue(component.consumeWelcomeIntro())
        component.signIn()
        component.state.value = UnAuthorizedProfileUiState.Loading
        component.state.value = UnAuthorizedProfileUiState.Error("")
        assertFalse(component.consumeWelcomeIntro())
    }

    @Test fun restoringTheSameScreenKeepsThePlayedFlag() {
        val keeper = StateKeeperDispatcher()
        assertTrue(component(keeper).consumeWelcomeIntro())
        val restored = component(StateKeeperDispatcher(keeper.save()))
        assertFalse(restored.consumeWelcomeIntro())
    }

    @Test fun savingBeforeTheGuestScreenAppearsDoesNotConsumeTheIntro() {
        val keeper = StateKeeperDispatcher()
        component(keeper)
        val restored = component(StateKeeperDispatcher(keeper.save()))
        assertTrue(restored.consumeWelcomeIntro())
        assertTrue(component(StateKeeperDispatcher()).consumeWelcomeIntro())
    }
}
