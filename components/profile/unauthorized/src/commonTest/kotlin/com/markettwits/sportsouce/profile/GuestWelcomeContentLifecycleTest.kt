package com.markettwits.sportsouce.profile

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher
import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User
import com.markettwits.sportsouce.unauthorized.domain.*
import com.markettwits.sportsouce.unauthorized.presentation.component.UnAuthorizedProfileComponentBase
import com.markettwits.sportsouce.unauthorized.presentation.components.UnAuthorizedProfileUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class GuestWelcomeContentLifecycleTest {
    @Test fun firstDownloadAppearsOnTheOpenScreenAndSurvivesReturningFromSignIn() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val lifecycle = LifecycleRegistry()
        try {
            val response = CompletableDeferred<GuestWelcomeContent>()
            val downloaded = GuestWelcomeContent.Local.copy(id = "trail", imageUrl = "https://cdn.example.test/trail.jpg")
            var cached = GuestWelcomeContent.Local
            var refreshes = 0
            val repo = object : GuestWelcomeRepository {
                override suspend fun cachedContent() = cached
                override suspend fun refresh() {
                    refreshes++
                    cached = response.await()
                }
            }
            val component = guestComponent(lifecycle, repo)
            lifecycle.resume()
            runCurrent()
            assertEquals(GuestWelcomeContent.Local, (component.state.value as UnAuthorizedProfileUiState.Error).welcome)

            lifecycle.stop(); lifecycle.resume(); runCurrent()
            assertEquals(1, refreshes, "Returning while downloading must reuse the in-flight refresh")
            response.complete(downloaded)
            runCurrent()
            assertEquals(downloaded, (component.state.value as UnAuthorizedProfileUiState.Error).welcome)

            cached = downloaded.copy(id = "winter")
            lifecycle.stop(); lifecycle.resume(); runCurrent()
            assertEquals(downloaded, (component.state.value as UnAuthorizedProfileUiState.Error).welcome)
        } finally {
            lifecycle.destroy()
            Dispatchers.resetMain()
        }
    }

    @Test fun aFirstOfflineVisitCanReceiveRemoteContentOnResumeWithoutRestarting() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val lifecycle = LifecycleRegistry()
        try {
            val downloaded = GuestWelcomeContent.Local.copy(id = "trail", imageUrl = "https://cdn.example.test/trail.jpg")
            var online = false
            var cached = GuestWelcomeContent.Local
            val repo = object : GuestWelcomeRepository {
                override suspend fun cachedContent() = cached
                override suspend fun refresh() {
                    if (online) cached = downloaded
                }
            }
            val component = guestComponent(lifecycle, repo)
            lifecycle.resume(); runCurrent()
            assertEquals(GuestWelcomeContent.Local, (component.state.value as UnAuthorizedProfileUiState.Error).welcome)
            online = true
            lifecycle.stop(); lifecycle.resume(); runCurrent()
            assertEquals(downloaded, (component.state.value as UnAuthorizedProfileUiState.Error).welcome)
        } finally {
            lifecycle.destroy()
            Dispatchers.resetMain()
        }
    }

    @Test fun leavingTheGuestScreenCancelsTheFirstDownload() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val lifecycle = LifecycleRegistry()
        try {
            val response = CompletableDeferred<GuestWelcomeContent>()
            var cached = GuestWelcomeContent.Local
            var cancelled = false
            val repo = object : GuestWelcomeRepository {
                override suspend fun cachedContent() = cached
                override suspend fun refresh() {
                    try {
                        cached = response.await()
                    } finally {
                        cancelled = true
                    }
                }
            }
            val component = guestComponent(lifecycle, repo)
            lifecycle.resume(); runCurrent()
            val before = component.state.value
            lifecycle.destroy(); runCurrent()
            response.complete(GuestWelcomeContent.Local.copy(id = "late"))
            runCurrent()
            assertTrue(cancelled)
            assertEquals(before, component.state.value)
        } finally {
            lifecycle.destroy()
            Dispatchers.resetMain()
        }
    }

    private fun guestComponent(lifecycle: LifecycleRegistry, repo: GuestWelcomeRepository) = UnAuthorizedProfileComponentBase(
        DefaultComponentContext(lifecycle),
        object : UnauthorizedProfileUseCase {
            override suspend fun authorize(): Result<User> = Result.failure(IllegalStateException("Guest"))
            override suspend fun exit() = Unit
        },
        goAuthProfile = {}, goSignIn = {}, welcomeRepository = repo,
    )

    @Test fun cachedVariantStaysStableAcrossSignInReturnAndStateRestoration() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        try {
            val first = GuestWelcomeContent.Local.copy(id = "trail")
            val next = GuestWelcomeContent.Local.copy(id = "winter")
            var selected = 0
            var cached = first
            val repo = object : GuestWelcomeRepository {
                override suspend fun cachedContent(): GuestWelcomeContent { selected++; return cached }
                override suspend fun refresh() { cached = next }
            }
            fun create(lifecycle: LifecycleRegistry, keeper: StateKeeperDispatcher) = UnAuthorizedProfileComponentBase(
                DefaultComponentContext(lifecycle, stateKeeper = keeper),
                object : UnauthorizedProfileUseCase {
                    override suspend fun authorize(): Result<User> = Result.failure(IllegalStateException("Guest"))
                    override suspend fun exit() = Unit
                }, goAuthProfile = {}, goSignIn = {}, welcomeRepository = repo,
            )
            val lifecycle = LifecycleRegistry()
            val keeper = StateKeeperDispatcher()
            val component = create(lifecycle, keeper)
            lifecycle.resume()
            runCurrent()
            fun current() = (component.state.value as UnAuthorizedProfileUiState.Error).welcome
            assertEquals(first, current())
            assertEquals(next, cached, "Refresh should prepare the next visit")
            lifecycle.stop(); lifecycle.resume(); runCurrent()
            assertEquals(first, current(), "Returning from sign-in must not replace the active text")
            assertEquals(1, selected)
            val restored = create(LifecycleRegistry(), StateKeeperDispatcher(keeper.save()))
            assertEquals(first, (restored.state.value as UnAuthorizedProfileUiState.Error).welcome)
            val anotherLifecycle = LifecycleRegistry()
            val another = create(anotherLifecycle, StateKeeperDispatcher())
            anotherLifecycle.resume(); runCurrent()
            assertEquals(next, (another.state.value as UnAuthorizedProfileUiState.Error).welcome)
        } finally { Dispatchers.resetMain() }
    }
}
