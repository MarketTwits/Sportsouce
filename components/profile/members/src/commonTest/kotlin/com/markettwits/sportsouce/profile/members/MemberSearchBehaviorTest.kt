package com.markettwits.sportsouce.profile.members

import com.markettwits.sportsouce.profile.members.member_common.domain.*
import com.markettwits.sportsouce.profile.members.member_search.MemberSearchController
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class MemberSearchBehaviorTest {
    @Test fun uniqueMatchStillNeedsAChoiceAndSendingCannotBeDuplicated() = runTest {
        val repository = FakeIdentity()
        val controller = MemberSearchController(repository, backgroundScope)
        runCurrent()
        controller.update { copy(name = "Анна", surname = "Иванова", birthYear = "2005") }
        controller.search(); runCurrent()
        assertEquals(0, repository.requests)
        controller.requestConnection(1282); controller.requestConnection(1282); runCurrent()
        assertEquals(1, repository.requests)
        repository.sendGate.complete(Result.success(MemberConnectionRequest(5, 1282, "pending")))
        runCurrent(); controller.requestConnection(1282); runCurrent()
        assertEquals(1, repository.requests)
        assertEquals("pending", controller.state.value.requests[1282]?.status)
    }

    @Test fun pendingAndAcceptedConnectionsAreRestoredBeforeAnyNewRequest() = runTest {
        for (status in listOf("pending", "accepted")) {
            val repository = FakeIdentity().apply { access = Result.success(MemberIdentityTeam(outgoing = listOf(MemberConnectionRequest(5, 1282, status)))) }
            val controller = MemberSearchController(repository, backgroundScope); runCurrent()
            controller.update { copy(name = "Анна", surname = "Иванова", birthYear = "2005") }
            controller.search(); runCurrent(); controller.requestConnection(1282); runCurrent()
            assertEquals(0, repository.requests)
        }
    }

    @Test fun aFailedAccessRefreshDoesNotEnableSendingAndCanBeRetried() = runTest {
        val repository = FakeIdentity().apply { access = Result.failure(IllegalStateException("offline")) }
        val controller = MemberSearchController(repository, backgroundScope); runCurrent()
        controller.update { copy(name = "Анна", surname = "Иванова", birthYear = "2005") }
        controller.search(); runCurrent(); controller.requestConnection(1282); runCurrent()
        assertEquals(0, repository.requests); assertFalse(controller.state.value.accessReady)
        repository.access = Result.success(MemberIdentityTeam())
        controller.refreshConnections(); runCurrent()
        assertTrue(controller.state.value.accessReady)
    }

    @Test fun changingQueryCancelsStaleResultsAndInvalidYearDoesNotCallTheServer() = runTest {
        val repository = FakeIdentity().apply { previewGate = CompletableDeferred() }
        val controller = MemberSearchController(repository, backgroundScope); runCurrent()
        controller.update { copy(name = "Анна", surname = "Иванова", birthYear = "2999") }
        controller.search(); runCurrent(); assertEquals(0, repository.searches)
        controller.update { copy(birthYear = "2005") }; controller.search(); runCurrent()
        assertTrue(controller.state.value.loading)
        controller.update { copy(name = "Другое имя") }
        repository.previewGate!!.complete(Result.success(result()))
        runCurrent()
        assertNull(controller.state.value.result); assertFalse(controller.state.value.loading)
    }

    private class FakeIdentity : MemberIdentityRepository {
        var access: Result<MemberIdentityTeam> = Result.success(MemberIdentityTeam())
        var searches = 0; var requests = 0
        var previewGate: CompletableDeferred<Result<MemberSearchResult>>? = null
        val sendGate = CompletableDeferred<Result<MemberConnectionRequest>>()
        override suspend fun team() = access
        override suspend fun search(name: String, surname: String, birthYear: Int, profileId: Int?): Result<MemberSearchResult> {
            searches++; return previewGate?.await() ?: Result.success(result())
        }
        override suspend fun requestConnection(userId: Int): Result<MemberConnectionRequest> { requests++; return sendGate.await() }
        override suspend fun respondToRequest(requestId: Int, accept: Boolean) = Result.success(Unit)
        override suspend fun updateRelation(userId: Int, relation: String?) = Result.success(Unit)
        override suspend fun updateAutoAccept(trustId: Int, enabled: Boolean) = Result.success(Unit)
        override suspend fun addTrustedPerson(userId: Int) = Result.success(Unit)
    }
    companion object {
        private fun result() = MemberSearchResult(listOf(MemberCandidate(1282, "Анна", "Иванова", 2005, null, true, "HIGH", emptyList())),
            "Найден однозначный профиль", emptyList(), false)
    }
}
