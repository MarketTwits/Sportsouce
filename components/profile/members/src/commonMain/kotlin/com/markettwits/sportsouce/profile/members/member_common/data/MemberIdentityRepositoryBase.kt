package com.markettwits.sportsouce.profile.members.member_common.data

import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkIdentityApi
import com.markettwits.sportsouce.profile.cloud.model.identity.*
import com.markettwits.sportsouce.profile.members.member_common.domain.*
import kotlinx.serialization.json.*
import kotlinx.datetime.number

class MemberIdentityRepositoryBase(
    private val api: SportSauceNetworkIdentityApi,
    private val auth: AuthDataSource,
) : MemberIdentityRepository {
    override suspend fun search(name: String, surname: String, birthYear: Int, profileId: Int?): Result<MemberSearchResult> = runCatching {
        val currentUserId = auth.user().getOrThrow().id
        val result = api.preview(IdentityPreviewRequest(surname.trim(), name.trim(), birthYear, "THIRD_PARTY_REGISTRATION", profileId), auth.updateToken().getOrThrow())
        MemberSearchResult(candidates = result.rows.map { it.toMemberCandidate(currentUserId) }.distinctBy { it.userId }, message = result.message.orEmpty(),
            clarificationHints = result.clarificationHints.mapNotNull { hint -> (hint as? JsonPrimitive)?.contentOrNull ?:
                ((hint as? JsonObject)?.get("label") as? JsonPrimitive)?.contentOrNull }, needsClarification = result.needsClarification)
    }

    override suspend fun respondToRequest(requestId: Int, accept: Boolean): Result<Unit> = runCatching {
        api.respondToRequest(requestId, accept, auth.updateToken().getOrThrow())
    }
    override suspend fun updateRelation(userId: Int, relation: String?): Result<Unit> = runCatching {
        require(relation == null || relation in memberRelationOptions) { "Неизвестный тип связи" }
        api.updateRelation(userId, relation, auth.updateToken().getOrThrow())
    }
    override suspend fun updateAutoAccept(trustId: Int, enabled: Boolean): Result<Unit> = runCatching {
        api.updateAutoAccept(trustId, enabled, auth.updateToken().getOrThrow())
    }
    override suspend fun addTrustedPerson(userId: Int): Result<Unit> = runCatching {
        api.addTrustedPerson(userId, auth.updateToken().getOrThrow())
    }

    override suspend fun team(): Result<MemberIdentityTeam> = runCatching {
        val response = api.team(auth.updateToken().getOrThrow())
        MemberIdentityTeam(
            trusted = response.trusted.mapNotNull { entry -> entry.trustedUser?.let { person -> MemberTeamAccess(entry.id, person.id, person.fullName(),
                response.relations[person.id.toString()], entry.autoAcceptRegistrations == true) } },
            myTeam = response.myTeam.mapNotNull { entry -> entry.ownerUser?.let { person -> MemberTeamAccess(entry.id, person.id, person.fullName(),
                response.relations[person.id.toString()], entry.autoAcceptRegistrations == true,
                person.registrationMember(response.relations[person.id.toString()])) } },
            incoming = response.requests.incoming.map { it.toMemberRequest() },
            outgoing = response.requests.outgoing.map { it.toMemberRequest() },
        )
    }

    override suspend fun requestConnection(userId: Int): Result<MemberConnectionRequest> = runCatching {
        require(userId > 0) { "Не удалось определить профиль участника" }
        val result = api.requestConnection(userId, auth.updateToken().getOrThrow())
        check(result.target?.id == userId) { "Сервер вернул запрос для другого участника" }
        result.toMemberRequest()
    }
}

/** Candidate identity must come from the response, never from the search input. */
internal fun IdentityPreviewCandidate.toMemberCandidate(currentUserId: Int): MemberCandidate {
    val person = user
    val cityName = when (val value = person.city) {
        is JsonPrimitive -> value.contentOrNull
        is JsonObject -> (value["name"] as? JsonPrimitive)?.contentOrNull
        else -> null
    }?.takeIf { it.isNotBlank() }
    val ownProfile = person.id == currentUserId
    val greyProfile = person.isGrey == true || person.accountType == "grey"
    return MemberCandidate(
        userId = person.id, name = person.name.orEmpty().trim(), surname = person.surname.orEmpty().trim(),
        birthYear = person.birthYear, city = cityName,
        canRequestConnection = person.id > 0 && !ownProfile && !greyProfile,
        confidence = confidenceLevel, reasons = reasons.map { it.label },
        unavailableReason = if (ownProfile) "Это ваш профиль" else if (greyProfile) "Профиль без аккаунта" else null,
    )
}

internal fun IdentitySubject.registrationMember(relation: String?): ProfileMember? {
    if (id <= 0 || name.isNullOrBlank() || surname.isNullOrBlank()) return null
    // Registration uses the calendar date from the API, without timezone conversion.
    val date = birthday?.let { runCatching { memberBirthDate(it.substringBefore('T')) }.getOrNull() }
    return ProfileMember(
        id = 0, userId = 0, name = name.orEmpty().trim(), surname = surname.orEmpty().trim(),
        email = email.orEmpty(), phone = phone.orEmpty(), gender = sex.orEmpty(), team = team.orEmpty(),
        birthday = date?.let { "${it.day.toString().padStart(2, '0')}.${it.month.number.toString().padStart(2, '0')}.${it.year}" }.orEmpty(),
        type = memberRelationOptions[relation].orEmpty(), child = false,
        identityKnown = true, participantUserId = id, city = city.orEmpty(), patronymic = patronymic,
        source = ProfileMember.Source.Team,
    )
}

private fun IdentitySubject.fullName(): String = listOfNotNull(surname, name).joinToString(" ").trim().ifBlank { "Профиль № $id" }
private fun TeamRequestResponse.toMemberRequest() = MemberConnectionRequest(id, target?.id ?: 0, status.lowercase(), requester?.id ?: 0,
    requester?.fullName() ?: "Профиль недоступен", target?.fullName() ?: "Профиль недоступен", createdAt)
