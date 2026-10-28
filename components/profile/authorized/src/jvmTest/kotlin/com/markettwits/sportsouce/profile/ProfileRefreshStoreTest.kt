package com.markettwits.sportsouce.profile

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.*
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.IntentAction
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.profile.authorized.authorized.domain.*
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.component.AuthorizedProfileComponentBase
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.store.*
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.recent.domain.StartRecentRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import java.lang.reflect.Proxy
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileRefreshStoreTest {
    @Test fun resumeAndNetworkErrorsKeepRegistrationsWithoutDuplicateLoadsOrBackgroundSpinner() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val lifecycle = LifecycleRegistry()
        try {
            val calls = mutableListOf<Boolean>()
            var gate = CompletableDeferred<Unit>()
            var fail = false
            var cached = profileSnapshot()
            val interactor = object : UserProfileInteractor {
                override suspend fun userProfile(forced: Boolean) = flow {
                    calls += forced
                    if (!forced) emit(cached)
                    gate.await()
                    if (fail) error("temporary server failure")
                    cached = profileSnapshot(registrationIds = listOf(11))
                    emit(cached)
                }
            }
            val component = AuthorizedProfileComponentBase(DefaultComponentContext(lifecycle), factory(interactor)) {}
            lifecycle.resume(); runCurrent()
            assertEquals(listOf(false), calls, "Bootstrap and lifecycle must not both request the profile")
            assertEquals(listOf(10), component.state.value.registrationIds())
            assertFalse(component.state.value.isRefreshing)
            repeat(3) { component.obtainEvent(AuthorizedProfileStore.Intent.Refresh) }; runCurrent()
            assertEquals(1, calls.size)
            gate.complete(Unit); runCurrent()
            assertEquals(listOf(11), component.state.value.registrationIds())

            fail = true; gate = CompletableDeferred()
            lifecycle.pause(); lifecycle.resume(); runCurrent()
            assertEquals(listOf(false, false), calls)
            assertFalse(component.state.value.isRefreshing)
            gate.complete(Unit); runCurrent()
            assertEquals(listOf(11), component.state.value.registrationIds())
            assertNotNull(component.state.value.error)
            assertFalse(component.state.value.isLoading)

            gate = CompletableDeferred(); fail = false
            component.obtainEvent(AuthorizedProfileStore.Intent.Retry); runCurrent()
            assertTrue(component.state.value.isRefreshing)
            assertEquals(listOf(11), component.state.value.registrationIds())
            gate.complete(Unit); runCurrent()
            assertFalse(component.state.value.isRefreshing)
            assertNull(component.state.value.error)
            assertEquals(listOf(11), component.state.value.registrationIds())
        } finally { lifecycle.destroy(); Dispatchers.resetMain() }
    }

    @Test fun manualRetryCancelsOlderRequestAndOnlyLatestResponseCanReplaceTheList() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        var calls = 0
        val gates = List(2) { CompletableDeferred<Unit>() }
        val interactor = object : UserProfileInteractor {
            override suspend fun userProfile(forced: Boolean) = flow {
                val index = calls++
                if (!forced) emit(profileSnapshot())
                gates[index].await()
                emit(profileSnapshot(registrationIds = listOf(11 + index)))
            }
        }
        val store = factory(interactor).create()
        try {
            store.accept(AuthorizedProfileStore.Intent.Refresh); runCurrent()
            store.accept(AuthorizedProfileStore.Intent.Retry); runCurrent()
            gates[1].complete(Unit); runCurrent()
            assertEquals(listOf(12), store.state.registrationIds())
            gates[0].complete(Unit); runCurrent()
            assertEquals(listOf(12), store.state.registrationIds())
            assertFalse(store.state.isLoading); assertFalse(store.state.isRefreshing)
        } finally { store.dispose(); Dispatchers.resetMain() }
    }

    @Test fun firstLoadFailureIsAnErrorAndSuccessfulEmptyResponseStillRemovesObsoleteRegistrations() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        var result: UserProfile? = null
        val interactor = object : UserProfileInteractor {
            override suspend fun userProfile(forced: Boolean) = flow { emit(result ?: error("offline")) }
        }
        val store = factory(interactor).create()
        try {
            store.accept(AuthorizedProfileStore.Intent.Refresh); runCurrent()
            assertNull(store.state.user); assertNotNull(store.state.error)
            result = profileSnapshot()
            store.accept(AuthorizedProfileStore.Intent.Retry); runCurrent()
            assertEquals(listOf(10), store.state.registrationIds())
            result = profileSnapshot(registrationIds = emptyList())
            store.accept(AuthorizedProfileStore.Intent.Refresh); runCurrent()
            assertTrue(store.state.registrationIds().isEmpty())
            assertNull(store.state.error)
        } finally { store.dispose(); Dispatchers.resetMain() }
    }

    private fun AuthorizedProfileStore.State.registrationIds() = user!!.activity.userRegistry.map { it.id }
    private fun factory(interactor: UserProfileInteractor): AuthorizedProfileStoreFactory {
        val recent = object : StartRecentRepository {
            override val recentStarts = MutableStateFlow(emptyList<StartsListItem>())
            override suspend fun add(start: StartsListItem) = Unit
            override suspend fun clear() = Result.success(Unit)
        }
        val tracker = Proxy.newProxyInstance(ExceptionTracker::class.java.classLoader, arrayOf(ExceptionTracker::class.java)) { _, _, _ -> null } as ExceptionTracker
        val action = Proxy.newProxyInstance(IntentAction::class.java.classLoader, arrayOf(IntentAction::class.java)) { _, method, _ ->
            error("Unexpected ${method.name}")
        } as IntentAction
        return AuthorizedProfileStoreFactory(DefaultStoreFactory(), interactor, recent, tracker, action)
    }
}
