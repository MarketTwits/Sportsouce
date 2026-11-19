package com.markettwits.sportsouce.profile.members.members_list.presentation.store.store

import com.markettwits.sportsouce.profile.members.member_common.domain.MemberIdentityRepository
import com.markettwits.sportsouce.profile.members.member_common.domain.MemberIdentityTeam
import com.markettwits.core.errors.api.throwable.mapToString
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.isNetworkConnectionError
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.profile.members.members_list.domain.MembersListUseCase
import com.markettwits.sportsouce.profile.members.members_list.presentation.store.store.MembersListStore.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive

class MembersListExecutor(
    private val useCase: MembersListUseCase,
    private val exceptionTracker: ExceptionTracker,
    private val identity: MemberIdentityRepository,
) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    private var listJob: Job? = null
    private var teamJob: Job? = null

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.GoBack -> publish(Label.GoBack)
            is Intent.OnClickSearchMember -> publish(Label.OnClickSearchMember)
            is Intent.OnClickAddMember -> publish(Label.OnClickAddMember)
            is Intent.OnClickMember -> publish(Label.OnClickMember(intent.member))
            is Intent.Retry -> if (state().actingTeamKey == null) launch(true)
            is Intent.RespondToRequest -> if (state().team?.incoming?.any { it.id == intent.id && it.status == "pending" } == true) {
                teamAction("request:${intent.id}", { identity.respondToRequest(intent.id, intent.accept) }) { team ->
                    team.copy(incoming = team.incoming.map { if (it.id == intent.id) it.copy(status = if (intent.accept) "accepted" else "rejected") else it })
                }
            }
            is Intent.UpdateRelation -> teamAction("relation:${intent.userId}", { identity.updateRelation(intent.userId, intent.relation) }) { team ->
                team.copy(myTeam = team.myTeam.map { if (it.userId == intent.userId) it.copy(relation = intent.relation) else it },
                    trusted = team.trusted.map { if (it.userId == intent.userId) it.copy(relation = intent.relation) else it })
            }
            is Intent.UpdateAutoAccept -> if (state().team?.trusted?.any { it.id == intent.trustId } == true) {
                teamAction("auto:${intent.trustId}", { identity.updateAutoAccept(intent.trustId, intent.enabled) }) { team ->
                    team.copy(trusted = team.trusted.map { if (it.id == intent.trustId) it.copy(autoAccept = intent.enabled) else it })
                }
            }
            is Intent.TrustPerson -> if (intent.userId !in state().trustedChangesAwaitingRefresh &&
                state().team?.myTeam?.any { it.userId == intent.userId } == true &&
                state().team?.trusted?.none { it.userId == intent.userId } == true) {
                teamAction("trust:${intent.userId}", {
                    identity.addTrustedPerson(intent.userId).onSuccess { dispatch(Message.TrustedPersonAdded(intent.userId)) }
                })
            }
            is Intent.RemoveMember -> dispatch(Message.Loaded(state().members.filterNot { it.id == intent.id }))
            is Intent.UpdateMember -> updateMember(intent.member, state().members)
        }
    }

    override fun executeAction(action: Unit) {
        launch(false)
    }

    private fun launch(forced: Boolean) {
        teamJob?.cancel()
        listJob?.cancel()
        teamJob = scope.launch {
            dispatch(Message.TeamLoading)
            val result = identity.team()
            ensureActive()
            result.fold(onSuccess = { dispatch(Message.TeamLoaded(it)) },
                onFailure = { dispatch(Message.TeamFailed(it.mapToSauceError().mapToString())) })
        }
        listJob = scope.launch {
            dispatch(Message.Loading)
            useCase.members(forced)
                .catch {
                    dispatch(Message.Error(it.mapToSauceError()))
                    if (!it.isNetworkConnectionError())
                        exceptionTracker.reportException(it, "MembersListExecutor#launch")
                }
                .collect { members ->
                    dispatch(Message.Loaded(members))
                }
        }
    }

    private fun teamAction(key: String, operation: suspend () -> Result<Unit>, update: (MemberIdentityTeam) -> MemberIdentityTeam = { it }) {
        if (state().actingTeamKey != null || state().teamLoading) return
        dispatch(Message.TeamActionLoading(key))
        scope.launch {
            operation().fold(onSuccess = {
                state().team?.let { team -> dispatch(Message.TeamLoaded(update(team))) }
                identity.team().fold(onSuccess = { dispatch(Message.TeamLoaded(it)) }, onFailure = {
                    dispatch(Message.TeamFailed("Изменения сохранены. Не удалось обновить связи — попробуйте ещё раз."))
                })
            }, onFailure = { dispatch(Message.TeamFailed(it.mapToSauceError().mapToString())) })
            dispatch(Message.TeamActionDone)
        }
    }

    private fun updateMember(member: ProfileMember, currentList: List<ProfileMember>) {
        val index = currentList.indexOfFirst { it.id == member.id }
        if (index >= 0) {
            val newList = currentList.toMutableList()
            newList[index] = member
            dispatch(Message.Loaded(newList))
        }
    }
}
