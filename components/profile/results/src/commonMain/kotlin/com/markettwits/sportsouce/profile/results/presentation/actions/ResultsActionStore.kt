package com.markettwits.sportsouce.profile.results.presentation.actions

import com.arkivanov.mvikotlin.core.store.*
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.sportsouce.profile.results.domain.*
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable

@Serializable
enum class ResultsAction { Find, Add, Requests }

sealed interface ResultMutation {
    data class Claim(val result: UnmatchedResult) : ResultMutation
    data class Candidate(val candidate: ResultCandidate, val action: String) : ResultMutation
    data class Suggestion(val request: ResultClaim, val accept: Boolean) : ResultMutation
    data class Conflict(val request: ResultClaim, val transfer: Boolean) : ResultMutation
    data class Reopen(val request: ResultClaim) : ResultMutation
    data object Discard : ResultMutation
}

interface ResultsActionStore : Store<ResultsActionStore.Intent, ResultsActionStore.State, ResultsActionStore.Label> {
    data class State(
        val action: ResultsAction,
        val draft: ExternalResultDraft = ExternalResultDraft(),
        val errors: Map<ResultField, String> = emptyMap(),
        val validationAttempt: Int = 0,
        val file: ResultProofFile? = null,
        val fileError: String? = null,
        val uploadedFileId: Int? = null,
        val startQuery: String = "",
        val startId: Int? = null,
        val starts: List<StartSuggestion> = emptyList(),
        val startsLoading: Boolean = false,
        val startError: String? = null,
        val query: String = "",
        val results: List<UnmatchedResult>? = null,
        val searching: Boolean = false,
        val claims: List<ResultClaim> = emptyList(),
        val submittedResults: Set<Int> = emptySet(),
        val candidates: List<ResultCandidate> = emptyList(),
        val dismissed: List<ResultCandidate> = emptyList(),
        val suggestions: List<ResultClaim> = emptyList(),
        val loading: Boolean = false,
        val loadError: String? = null,
        val error: String? = null,
        val busy: Boolean = false,
        val success: String? = null,
        val confirmation: ResultMutation? = null,
    )
    sealed interface Intent {
        data object Close : Intent
        data object Reload : Intent
        data class Field(val field: ResultField, val value: String) : Intent
        data class File(val value: ResultProofFile?, val error: String? = null) : Intent
        data object Save : Intent
        data class StartQuery(val value: String) : Intent
        data class SelectStart(val value: StartSuggestion) : Intent
        data class Query(val value: String) : Intent
        data object Search : Intent
        data class Confirm(val mutation: ResultMutation) : Intent
        data object CancelConfirmation : Intent
        data object SubmitConfirmation : Intent
    }
    sealed interface Label { data object Close : Label; data object Changed : Label }
}

internal fun resultsActionStore(factory: StoreFactory, api: ResultsActionsRepository, action: ResultsAction, categories: List<ResultCategory>): ResultsActionStore =
    object : ResultsActionStore, Store<ResultsActionStore.Intent, ResultsActionStore.State, ResultsActionStore.Label> by factory.create(
        name = "ResultsAction-$action", initialState = ResultsActionStore.State(action), bootstrapper = SimpleBootstrapper(Unit),
        executorFactory = { ResultsActionExecutor(api, categories) },
        reducer = object : Reducer<ResultsActionStore.State, ResultsActionStore.State> {
            override fun ResultsActionStore.State.reduce(msg: ResultsActionStore.State) = msg
        },
    ) {}

private class ResultsActionExecutor(private val api: ResultsActionsRepository, private val categories: List<ResultCategory>) : CoroutineExecutor<ResultsActionStore.Intent, Unit, ResultsActionStore.State, ResultsActionStore.State, ResultsActionStore.Label>() {
    private var startsJob: Job? = null
    private var searchJob: Job? = null
    private var loadJob: Job? = null
    private var uploadId: Int? = null
    override fun executeAction(action: Unit) { if (state().action != ResultsAction.Add) reload() }
    override fun executeIntent(intent: ResultsActionStore.Intent) {
        if (state().busy) return
        if (state().action == ResultsAction.Add && state().success != null && intent != ResultsActionStore.Intent.Close) return
        when (intent) {
            ResultsActionStore.Intent.Close -> if (state().action == ResultsAction.Add && state().success == null && (state().draft != ExternalResultDraft() || state().file != null)) update { copy(confirmation = ResultMutation.Discard) } else publish(ResultsActionStore.Label.Close)
            ResultsActionStore.Intent.Reload -> reload()
            is ResultsActionStore.Intent.Field -> update { copy(draft = draft.update(intent.field, intent.value), errors = errors - intent.field, error = null) }
            is ResultsActionStore.Intent.File -> { uploadId = null; update { copy(file = intent.value?.takeIf { it.validationError() == null }, fileError = intent.error ?: intent.value?.validationError(), uploadedFileId = null, errors = errors - ResultField.Proof) } }
            ResultsActionStore.Intent.Save -> save()
            is ResultsActionStore.Intent.StartQuery -> starts(intent.value)
            is ResultsActionStore.Intent.SelectStart -> { startsJob?.cancel(); searchJob?.cancel(); update { copy(startQuery = intent.value.name, startId = intent.value.id, starts = emptyList(), results = null, startsLoading = false, searching = false, startError = null) } }
            is ResultsActionStore.Intent.Query -> { searchJob?.cancel(); update { copy(query = intent.value, searching = false, results = null, error = null) } }
            ResultsActionStore.Intent.Search -> search()
            is ResultsActionStore.Intent.Confirm -> update { copy(confirmation = intent.mutation, error = null) }
            ResultsActionStore.Intent.CancelConfirmation -> update { copy(confirmation = null) }
            ResultsActionStore.Intent.SubmitConfirmation -> state().confirmation?.let(::mutate)
        }
    }
    private inline fun update(block: ResultsActionStore.State.() -> ResultsActionStore.State) = dispatch(state().block())
    private fun starts(value: String) {
        startsJob?.cancel(); searchJob?.cancel()
        update { copy(startQuery = value, startId = value.toIntOrNull()?.takeIf { it > 0 }, starts = emptyList(), startsLoading = value.length >= 2, startError = null, results = null, searching = false) }
        if (value.length < 2) return
        startsJob = scope.launch {
            delay(300)
            try { val items = api.starts(value); ensureActive(); update { copy(starts = items, startsLoading = false) } }
            catch (e: CancellationException) { throw e }
            catch (e: Exception) { update { copy(startsLoading = false, startError = e.messageForUser()) } }
        }
    }
    private fun search() {
        if (state().startQuery.isNotBlank() && state().startId == null) { update { copy(startError = "Выберите старт из списка или введите его ID") }; return }
        if (state().startId == null && (state().query.trim().length < 2 && state().query.toIntOrNull() == null)) { update { copy(error = "Введите имя (от 2 символов) или стартовый номер") }; return }
        searchJob?.cancel()
        val query = state().query; val start = state().startId
        update { copy(searching = true, results = null, error = null) }
        searchJob = scope.launch {
            try { val items = api.search(query, start); ensureActive(); update { copy(results = items, searching = false) } }
            catch (e: CancellationException) { throw e }
            catch (e: Exception) { update { copy(searching = false, error = e.messageForUser()) } }
        }
    }
    private fun reload() {
        loadJob?.cancel()
        update { copy(loading = true, loadError = null) }
        loadJob = scope.launch {
            val failures = mutableListOf<String>()
            suspend fun <T> part(load: suspend () -> T, apply: (T) -> Unit) {
                try { val value = load(); ensureActive(); apply(value) }
                catch (e: CancellationException) { throw e }
                catch (e: Exception) { failures += e.messageForUser() }
            }
            part(api::claims) { value -> update { copy(claims = value) } }
            if (state().action == ResultsAction.Requests) {
                part({ api.candidates() }) { value -> update { copy(candidates = value.filter { it.status == null || it.status == "pending" }) } }
                part({ api.candidates(true) }) { value -> update { copy(dismissed = value) } }
                part(api::suggestions) { value -> update { copy(suggestions = value) } }
            }
            update { copy(loading = false, loadError = failures.firstOrNull()) }
        }
    }
    private fun save() {
        val errors = state().draft.errors(state().file != null)
        if (errors.isNotEmpty()) { update { copy(errors = errors, validationAttempt = validationAttempt + 1) }; return }
        val draft = state().draft; val file = state().file
        update { copy(busy = true, error = null) }
        scope.launch {
            try {
                // Keep the uploaded id for a retry when submitting the result fails.
                if (file != null && uploadId == null) { uploadId = api.upload(file); ensureActive(); update { copy(uploadedFileId = uploadId) } }
                api.add(draft.request(categories, uploadId)); ensureActive()
                update { copy(busy = false, success = "Результат отправлен на проверку. Он появится в статистике после подтверждения.", file = null) }
                publish(ResultsActionStore.Label.Changed)
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { update { copy(busy = false, error = e.messageForUser()) } }
        }
    }
    private fun mutate(mutation: ResultMutation) {
        if (mutation == ResultMutation.Discard) { publish(ResultsActionStore.Label.Close); return }
        loadJob?.cancel()
        update { copy(busy = true, confirmation = null, error = null, success = null, loading = false) }
        scope.launch {
            try {
                when (mutation) {
                    is ResultMutation.Claim -> api.claim(mutation.result.id)
                    is ResultMutation.Candidate -> api.candidate(mutation.candidate.id, mutation.action)
                    is ResultMutation.Suggestion -> api.suggestion(mutation.request.id, mutation.accept)
                    is ResultMutation.Conflict -> api.conflict(mutation.request.id, mutation.transfer)
                    is ResultMutation.Reopen -> api.claim(requireNotNull(mutation.request.result?.id))
                    ResultMutation.Discard -> Unit
                }
                ensureActive()
                // Mark a submitted claim immediately, even if the following refresh is offline.
                if (mutation is ResultMutation.Claim) update { copy(submittedResults = submittedResults + mutation.result.id, claims = claims + ResultClaim(-mutation.result.id, "USER_CLAIM", "PENDING", result = ClaimedResult(mutation.result.id, mutation.result.name))) }
                update { copy(busy = false, success = "Действие выполнено") }
                publish(ResultsActionStore.Label.Changed)
                reload()
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { update { copy(busy = false, error = e.messageForUser()) } }
        }
    }
}
private fun Throwable.messageForUser() = mapToSauceError().mapToString()
