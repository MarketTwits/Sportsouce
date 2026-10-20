package com.markettwits.sportsouce.profile.members.member_common.domain

data class MemberCandidate(
    val userId: Int,
    val name: String,
    val surname: String,
    val birthYear: Int?,
    val city: String?,
    val canRequestConnection: Boolean,
    val confidence: String,
    val reasons: List<String>,
    val unavailableReason: String? = null,
)

data class MemberSearchResult(
    val candidates: List<MemberCandidate>,
    val message: String,
    val clarificationHints: List<String>,
    val needsClarification: Boolean,
)

data class MemberConnectionRequest(
    val id: Int,
    val targetUserId: Int,
    val status: String,
    val requesterId: Int = 0,
    val requesterName: String = "",
    val targetName: String = "",
    val createdAt: String? = null,
)

data class MemberTeamAccess(
    val id: Int, val userId: Int, val name: String, val relation: String?, val autoAccept: Boolean,
    val registrationMember: ProfileMember? = null,
)

data class MemberIdentityTeam(
    val trusted: List<MemberTeamAccess> = emptyList(),
    val myTeam: List<MemberTeamAccess> = emptyList(),
    val incoming: List<MemberConnectionRequest> = emptyList(),
    val outgoing: List<MemberConnectionRequest> = emptyList(),
)

interface MemberIdentityRepository {
    suspend fun respondToRequest(requestId: Int, accept: Boolean): Result<Unit>
    suspend fun updateRelation(userId: Int, relation: String?): Result<Unit>
    suspend fun updateAutoAccept(trustId: Int, enabled: Boolean): Result<Unit>
    suspend fun addTrustedPerson(userId: Int): Result<Unit>
    suspend fun team(): Result<MemberIdentityTeam>
    suspend fun search(name: String, surname: String, birthYear: Int, profileId: Int? = null): Result<MemberSearchResult>
    suspend fun requestConnection(userId: Int): Result<MemberConnectionRequest>
}

val memberRelationOptions = linkedMapOf(
    "relative" to "Родственник", "friend" to "Друг", "coach" to "Тренер", "student" to "Ученик", "teammate" to "Партнёр по команде",
)
