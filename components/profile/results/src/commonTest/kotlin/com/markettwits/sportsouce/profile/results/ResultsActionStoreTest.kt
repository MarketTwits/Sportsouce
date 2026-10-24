package com.markettwits.sportsouce.profile.results

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.profile.results.data.ResultsActionsApi
import com.markettwits.sportsouce.profile.results.data.ResultsActionsRepositoryBase
import com.markettwits.sportsouce.profile.results.domain.*
import com.markettwits.sportsouce.profile.results.presentation.actions.*
import com.markettwits.sportsouce.profile.results.presentation.actions.ResultsActionStore.Intent
import io.ktor.http.*
import io.ktor.client.engine.mock.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ResultsActionStoreTest {
    @BeforeTest fun main() { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    @AfterTest fun reset() { Dispatchers.resetMain() }

    @Test fun invalidFormHasFieldErrorsAndSuccessfulSubmitCannotRepeat() = runTest {
        val response = CompletableDeferred<Unit>()
        val provider = TestClient { request ->
            if (request.method == HttpMethod.Post) response.await()
            jsonResponse("{}")
        }
        val store = resultsActionStore(DefaultStoreFactory(), ResultsActionsRepositoryBase(ResultsActionsApi(provider)) { "token" }, ResultsAction.Add, emptyList())
        try {
            store.accept(Intent.Save)
            assertTrue(store.state.errors.isNotEmpty()); assertTrue(provider.requests.isEmpty())
            listOf(ResultField.Title to "Осенний кросс", ResultField.Date to "2026-10-20", ResultField.Distance to "8,5",
                ResultField.Time to "42:18", ResultField.Proof to "https://example.test/result").forEach { (field, value) -> store.accept(Intent.Field(field, value)) }
            store.accept(Intent.Save); store.accept(Intent.Save); runCurrent()
            assertTrue(store.state.busy); assertEquals(1, provider.requests.size)
            store.accept(Intent.Field(ResultField.Title, "Другое имя"))
            assertEquals("Осенний кросс", store.state.draft.title)
            response.complete(Unit); runCurrent()
            assertNotNull(store.state.success)
            store.accept(Intent.Save); runCurrent()
            assertEquals(1, provider.requests.size)
        } finally { store.dispose(); provider.client.close() }
    }

    @Test fun claimRequiresConfirmationAndDuplicateRequestsAreSuppressedWhilePending() = runTest {
        val response = CompletableDeferred<Unit>()
        val provider = TestClient { request ->
            if (request.method == HttpMethod.Post) { response.await(); jsonResponse("{}") }
            else jsonResponse("{\"rows\":[]}")
        }
        val store = resultsActionStore(DefaultStoreFactory(), ResultsActionsRepositoryBase(ResultsActionsApi(provider)) { "token" }, ResultsAction.Find, emptyList())
        try {
            runCurrent()
            val result = UnmatchedResult(42, "Иван Иванов")
            store.accept(Intent.Confirm(ResultMutation.Claim(result)))
            assertEquals(0, provider.requests.count { it.method == HttpMethod.Post })
            store.accept(Intent.SubmitConfirmation); store.accept(Intent.SubmitConfirmation); runCurrent()
            assertTrue(store.state.busy)
            assertEquals(1, provider.requests.count { it.method == HttpMethod.Post })
            response.complete(Unit); runCurrent()
            assertFalse(store.state.busy)
            assertTrue(42 in store.state.submittedResults)
        } finally { store.dispose(); provider.client.close() }
    }

    @Test fun failedUploadSubmissionRetainsDraftAndReusesUploadedFileOnRetry() = runTest {
        var attempts = 0
        var uploads = 0
        val repo = object : ResultsActionsRepository {
            override suspend fun upload(file: ResultProofFile): Int { uploads++; return 7 }
            override suspend fun add(request: ExternalResultRequest) { attempts++; assertEquals(7, request.proofFileId); if (attempts == 1) error("offline") }
            override suspend fun starts(query: String) = emptyList<StartSuggestion>()
            override suspend fun search(query: String, startId: Int?) = emptyList<UnmatchedResult>()
            override suspend fun claims() = emptyList<ResultClaim>()
            override suspend fun candidates(dismissed: Boolean) = emptyList<ResultCandidate>()
            override suspend fun suggestions() = emptyList<ResultClaim>()
            override suspend fun claim(id: Int) = Unit
            override suspend fun candidate(id: Int, action: String) = Unit
            override suspend fun suggestion(id: Int, accept: Boolean) = Unit
            override suspend fun conflict(id: Int, transfer: Boolean) = Unit
        }
        val store = resultsActionStore(DefaultStoreFactory(), repo, ResultsAction.Add, emptyList())
        try {
            listOf(ResultField.Title to "Кросс", ResultField.Date to "2026-10-20", ResultField.Distance to "5", ResultField.Time to "18:20").forEach { (field, value) -> store.accept(Intent.Field(field, value)) }
            store.accept(Intent.File(ResultProofFile("protocol.pdf", "application/pdf", byteArrayOf(1,2,3), 1)))
            store.accept(Intent.Save); runCurrent()
            assertNotNull(store.state.error); assertNull(store.state.success); assertEquals("Кросс", store.state.draft.title)
            store.accept(Intent.Save); runCurrent()
            assertNotNull(store.state.success)
            assertEquals(1, uploads)
            assertEquals(2, attempts)
        } finally { store.dispose() }
    }
}
