package com.markettwits.sportsouce.profile.cloud.model.identity

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class IdentityPreviewRequest(val surname: String, val name: String, val birthYear: Int,
    val context: String, val profileId: Int? = null)

@Serializable
data class IdentityRegistrationPreview(
    val context: String = "THIRD_PARTY_REGISTRATION",
    val unique: Boolean = false,
    val needsClarification: Boolean = false,
    val message: String? = null,
    val clarificationHints: List<JsonElement> = emptyList(),
    val rows: List<IdentityPreviewCandidate> = emptyList(),
    val subject: IdentitySubject? = null,
)

@Serializable
data class IdentitySubject(
    val id: Int,
    val name: String? = null,
    val surname: String? = null,
    val patronymic: String? = null,
    val birthday: String? = null,
    @Serializable(with = NullableIntOrStringSerializer::class) val birthYear: Int? = null,
    val sex: String? = null,
    val city: String? = null,
    val team: String? = null,
    val email: String? = null,
    val phone: String? = null,
)

@Serializable
data class IdentityPreviewCandidate(
    val user: IdentityPreviewUser,
    val confidenceLevel: String = "",
    val displayScore: Int? = null,
    val reasons: List<IdentityMatchReason> = emptyList(),
    val phoneMatched: Boolean = false,
    val emailMatched: Boolean = false,
)

@Serializable
data class IdentityPreviewUser(
    val id: Int,
    val name: String? = null,
    val surname: String? = null,
    @Serializable(with = NullableIntOrStringSerializer::class) val birthYear: Int? = null,
    val city: JsonElement? = null,
    val publicStartName: String? = null,
    val isGrey: Boolean? = null,
    val accountType: String? = null,
)

@Serializable
data class IdentityMatchReason(val code: String = "", val label: String, val positive: Boolean = false)

@Serializable
data class CreateTeamRequest(val targetUserId: Int)

@Serializable
data class TeamRequestResponse(
    val id: Int,
    val status: String,
    val comment: String? = null,
    val requester: IdentitySubject? = null,
    val target: IdentitySubject? = null,
    val createdAt: String? = null,
)

@Serializable
data class IdentityTeamResponse(
    val trusted: List<TeamRelationResponse> = emptyList(),
    val myTeam: List<TeamRelationResponse> = emptyList(),
    val requests: TeamRequestsResponse = TeamRequestsResponse(),
    val relations: Map<String, String> = emptyMap(),
)

@Serializable
data class TeamRelationResponse(
    val id: Int,
    val trustedUser: IdentitySubject? = null,
    val ownerUser: IdentitySubject? = null,
    val autoAcceptRegistrations: Boolean? = null,
)

@Serializable
data class TeamRequestsResponse(
    val incoming: List<TeamRequestResponse> = emptyList(),
    val outgoing: List<TeamRequestResponse> = emptyList(),
)

@Serializable
data class UpdateTeamRelation(val relation: String?)

@Serializable
data class UpdateAutoAccept(val autoAcceptRegistrations: Boolean)

@Serializable
data class AddTrustedPerson(val trustedUserId: Int)
