package com.markettwits.sportsouce.profile.members

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.profile.members.member_common.domain.*
import com.markettwits.sportsouce.profile.members.members_list.domain.MembersListUseCase
import com.markettwits.sportsouce.profile.members.members_list.presentation.store.store.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class MemberTeamBehaviorTest {
    @Test fun acceptingCannotRepeatAndARefreshFailureKeepsTheAcceptedState() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        try {
            val repository = FakeIdentity()
            val store = factory(repository).create()
            store.accept(MembersListStore.Intent.RespondToRequest(6, true))
            store.accept(MembersListStore.Intent.RespondToRequest(6, true))
            assertEquals(1, repository.responses)
            repository.currentTeam = Result.failure(IllegalStateException("refresh failed"))
            repository.gate.complete(Result.success(Unit))
            assertEquals("accepted", store.state.team?.incoming?.single()?.status)
            assertNotNull(store.state.teamError); assertNull(store.state.actingTeamKey)
            store.accept(MembersListStore.Intent.RespondToRequest(6, true))
            assertEquals(1, repository.responses)
            store.dispose()
        } finally { Dispatchers.resetMain() }
    }

    @Test fun autoAcceptUsesTheTrustIdAndFailureDoesNotChangePermission() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        try {
            val repository = FakeIdentity()
            val store = factory(repository).create()
            store.accept(MembersListStore.Intent.UpdateAutoAccept(4, false))
            assertEquals(4 to false, repository.autoRequest)
            assertTrue(store.state.team!!.trusted.single().autoAccept)
            repository.gate.complete(Result.failure(IllegalStateException("denied")))
            assertTrue(store.state.team!!.trusted.single().autoAccept)
            assertNotNull(store.state.teamError)
            store.dispose()
        } finally { Dispatchers.resetMain() }
    }

    private fun factory(repository: MemberIdentityRepository) = MembersListStoreFactory(
        DefaultStoreFactory(), object : MembersListUseCase {
            override suspend fun members(forced: Boolean) = flowOf(emptyList<ProfileMember>())
        }, object : ExceptionTracker {
            override fun reportException(exception: Throwable, key: String?) {}
            override fun setLog(message: String) {}
            override fun setUserId(userId: String) {}
            override fun setKey(key: Pair<String, String>) {}
        }, repository,
    )
    private class FakeIdentity : MemberIdentityRepository {
        var responses = 0
        var autoRequest: Pair<Int, Boolean>? = null
        val gate = CompletableDeferred<Result<Unit>>()
        var currentTeam: Result<MemberIdentityTeam> = Result.success(MemberIdentityTeam(
            incoming = listOf(MemberConnectionRequest(6, 1775, "pending", 2310, "Иван Иванов", "Анна Иванова")),
            trusted = listOf(MemberTeamAccess(4, 2310, "Иван Иванов", "coach", true)),
        ))
        override suspend fun team() = currentTeam
        override suspend fun respondToRequest(requestId: Int, accept: Boolean): Result<Unit> { responses++; return gate.await() }
        override suspend fun updateAutoAccept(trustId: Int, enabled: Boolean): Result<Unit> { autoRequest = trustId to enabled; return gate.await() }
        override suspend fun updateRelation(userId: Int, relation: String?) = Result.success(Unit)
        override suspend fun addTrustedPerson(userId: Int) = Result.success(Unit)
        override suspend fun search(name: String, surname: String, birthYear: Int, profileId: Int?) = Result.success(MemberSearchResult(emptyList(), "", emptyList(), false))
        override suspend fun requestConnection(userId: Int) = Result.success(MemberConnectionRequest(5, userId, "pending"))
    }
}
