package com.markettwits.sportsouce.profile.members.member_search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.sportsouce.profile.members.member_common.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class MemberSearchState(
    val name: String = "",
    val surname: String = "",
    val birthYear: String = "",
    val profileId: String = "",
    val loading: Boolean = false,
    val sendingUserId: Int? = null,
    val result: MemberSearchResult? = null,
    val requests: Map<Int, MemberConnectionRequest> = emptyMap(),
    val error: String? = null,
    val accessReady: Boolean = false,
    val accessError: String? = null,
    val connectedUserIds: Set<Int> = emptySet(),
)

class MemberSearchComponent(
    componentContext: ComponentContext,
    repository: MemberIdentityRepository,
    val back: () -> Unit,
    val addManually: () -> Unit,
) : ComponentContext by componentContext {
    private val controller = instanceKeeper.getOrCreate { MemberSearchController(repository) }
    val state: StateFlow<MemberSearchState> get() = controller.state
    fun updateName(value: String) = controller.update { copy(name = value) }
    fun updateSurname(value: String) = controller.update { copy(surname = value) }
    fun updateYear(value: String) = controller.update { copy(birthYear = value.filter(Char::isDigit).take(4)) }
    fun updateProfileId(value: String) = controller.update { copy(profileId = value.filter(Char::isDigit).take(10)) }
    fun search() = controller.search()
    fun refreshConnections() = controller.refreshConnections()
    fun requestConnection(userId: Int) = controller.requestConnection(userId)
}

internal class MemberSearchController(
    private val repository: MemberIdentityRepository,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate),
) : InstanceKeeper.Instance {
    private val mutableState = MutableStateFlow(MemberSearchState())
    val state: StateFlow<MemberSearchState> = mutableState.asStateFlow()
    private var searchJob: Job? = null

    init { refreshConnections() }

    fun refreshConnections() {
        scope.launch {
            mutableState.update { it.copy(accessError = null, accessReady = false) }
            val result = repository.team()
            ensureActive()
            result.fold(onSuccess = { team ->
                mutableState.update { it.copy(accessReady = true,
                    requests = team.outgoing.filter { it.status in listOf("pending", "accepted") }.associateBy { it.targetUserId },
                    connectedUserIds = team.myTeam.map { it.userId }.toSet()) }
            }, onFailure = { error -> mutableState.update { it.copy(accessError = error.mapToSauceError().mapToString()) } })
        }
    }

    fun update(transform: MemberSearchState.() -> MemberSearchState) {
        if (state.value.sendingUserId != null) return
        searchJob?.cancel()
        mutableState.update { it.transform().copy(result = null, error = null, loading = false) }
    }

    @OptIn(ExperimentalTime::class)
    fun search() {
        val input = state.value
        if (input.loading || input.sendingUserId != null) return
        val year = input.birthYear.toIntOrNull()
        if (input.profileId.isNotBlank() && (input.profileId.toIntOrNull() ?: 0) <= 0) {
            mutableState.update { it.copy(error = "Укажите корректный ID профиля") }; return
        }
        val currentYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
        if (input.name.isBlank() || input.surname.isBlank() || input.birthYear.length != 4 || year == null || year !in 1900..currentYear) {
            mutableState.update { it.copy(error = "Укажите имя, фамилию и корректный год рождения") }
            return
        }
        mutableState.update { it.copy(loading = true, error = null, result = null) }
        searchJob = scope.launch {
            val result = repository.search(input.name, input.surname, year, input.profileId.toIntOrNull())
            ensureActive()
            result.fold(onSuccess = { result -> mutableState.update { it.copy(result = result, loading = false) } },
                onFailure = { error -> mutableState.update { it.copy(loading = false, error = error.mapToSauceError().mapToString()) } })
        }
    }

    fun requestConnection(userId: Int) {
        val input = state.value
        if (!input.accessReady || userId in input.connectedUserIds || input.sendingUserId != null || input.loading || input.requests.containsKey(userId) ||
            input.result?.candidates?.none { it.userId == userId && it.canRequestConnection } != false) return
        mutableState.update { it.copy(sendingUserId = userId, error = null) }
        scope.launch {
            val result = repository.requestConnection(userId)
            ensureActive()
            result.fold(onSuccess = { request ->
                mutableState.update { it.copy(sendingUserId = null, requests = it.requests + (userId to request)) }
            }, onFailure = { error ->
                mutableState.update { it.copy(sendingUserId = null, error = error.mapToSauceError().mapToString()) }
            })
        }
    }

    override fun onDestroy() { scope.cancel() }
}
