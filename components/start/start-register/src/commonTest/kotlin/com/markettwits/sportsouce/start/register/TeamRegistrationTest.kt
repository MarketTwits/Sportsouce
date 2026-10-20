package com.markettwits.sportsouce.start.register

import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.profile.members.member_common.domain.emptyProfileMember
import com.markettwits.sportsouce.start.cloud.model.register.price.StartRegisterPriceRequest
import com.markettwits.sportsouce.start.register.domain.*
import com.markettwits.sportsouce.start.register.presentation.registration.common.data.mapper.StartRegisterPriceMapper
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.*
import com.markettwits.sportsouce.start.register.presentation.registration.member.components.*
import com.markettwits.sportsouce.start.register.presentation.registration.member.domain.mergeRegistrationParticipants
import com.markettwits.sportsouce.start.register.presentation.registration.member.domain.isPresentInProfileMembers
import com.markettwits.sportsouce.start.register.presentation.registration.member.store.*
import kotlinx.serialization.json.*
import kotlin.test.*

class TeamRegistrationTest {
    private val team = emptyProfileMember.copy(name = "Дмитрий", surname = "Сергеев", birthday = "17.01.1972",
        gender = "Мужской", team = "СпортСоюз", city = "Новосибирск", phone = "79000000001", email = "runner@example.test",
        participantUserId = 1775, source = ProfileMember.Source.Team)
    private val manual = team.copy(id = 2957, userId = 2310, participantUserId = null, source = ProfileMember.Source.Saved)
    private val self = team.copy(id = 2310, userId = 2310, participantUserId = 2310, source = ProfileMember.Source.Self, name = "Иван")

    @Test fun confirmedTeamMemberIsMergedByAccountIdWithoutCollapsingNamesakes() {
        val linked = manual.copy(participantUserId = 1775)
        val choices = mergeRegistrationParticipants(listOf(manual, linked, self), listOf(team, team, self))
        assertEquals(listOf(self, team, manual), choices)
        assertEquals("Сергеев Дмитрий (команда)", team.registrationLabel())
        assertEquals("Сергеев Иван (я)", self.registrationLabel())
    }

    @Test fun selectionReplacesAllParticipantFieldsAndDoesNotInheritTheTemplateOwner() {
        val current = statement().copy(stageId = 491, promocode = "PROMO", contactPerson = false)
        val selected = memberSelectApply(team, current)
        assertEquals(1775, selected.participantUserId)
        assertEquals("Новосибирск", selected.city)
        assertEquals("17.01.1972", selected.birthday)
        assertEquals("79000000001", selected.phone)
        assertEquals(491, selected.stageId); assertEquals("PROMO", selected.promocode); assertFalse(selected.contactPerson)
        assertEquals(StartStatement.UserId.WithoutId, selected.userId)
        assertTrue(selected.isPresentInProfileMembers(listOf(team)))
        val saved = memberSelectApply(manual.copy(phone = "", email = "", city = ""), selected)
        assertNull(saved.participantUserId); assertEquals("", saved.city); assertEquals("", saved.phone)
        assertEquals(StartStatement.UserId.WithoutId, saved.userId)
        assertEquals(ProfileMember.Source.Saved, saved.participantSource)
    }

    @Test fun identityChangesDetachAnAccountButContactChangesKeepTheExplicitSelection() {
        val selected = memberSelectApply(team, statement())
        for (changed in listOf(selected.copy(name = "Другой"), selected.copy(surname = "Другой"), selected.copy(birthday = "18.01.1972"))) {
            val detached = changed.withIdentityDetachedIfChanged(selected)
            assertNull(detached.participantUserId); assertEquals(ProfileMember.Source.Saved, detached.participantSource)
            assertEquals(StartStatement.UserId.WithoutId, detached.userId)
        }
        assertEquals(1775, selected.copy(city = "Бердск", email = "new@example.test").withIdentityDetachedIfChanged(selected).participantUserId)
        assertEquals(1775, selected.copy(name = " Дмитрий ").withIdentityDetachedIfChanged(selected).participantUserId)
    }

    @Test fun priceAndSubmitPayloadCarryANumericParticipantIdAndAnUnshiftedDate() {
        val selected = memberSelectApply(team, statement()).copy(patronymic = "Иванович")
        val mapper = StartRegisterPriceMapper(BaseTimeMapper())
        val distance = StartRegistrationDistance(468, "", "Ретро кросс", listOf(StartRegistrationStageWithStatement(selected,
            StartRegistrationStage(491, 468, "", "", "", emptyList()))), emptyList(), StartRegistrationFieldPrice.Empty, "")
        val json = Json { explicitNulls = false }
        for (withoutPayment in listOf(null, false, true)) {
            val request = mapper.mapPrice(null, 326, "", StartRegType.Default, withoutPayment, listOf(distance))
            val payload = json.parseToJsonElement(json.encodeToString(StartRegisterPriceRequest.serializer(), request)).jsonObject
            if (withoutPayment == null) assertFalse(payload.containsKey("registration_without_payment"))
            else assertEquals(JsonPrimitive(withoutPayment), payload["registration_without_payment"])
            val member = payload["distances"]!!.jsonArray.single().jsonObject["member"]!!.jsonArray.single().jsonObject
            assertEquals(JsonPrimitive(1775), member["participant_user_id"])
            assertEquals("1972-01-17", member["birthday"]!!.jsonPrimitive.content)
            assertEquals("Новосибирск", member["city"]!!.jsonPrimitive.content)
            assertEquals(491, member["stage_id"]!!.jsonPrimitive.int)
            assertEquals("Иванович", member["patronymic"]!!.jsonPrimitive.content)
            assertFalse(member.containsKey("user_id"))
            assertTrue(member["answers"]!!.jsonArray.isEmpty())
        }
        val manualDistance = distance.copy(stages = listOf(distance.stages.single().copy(statement = memberSelectApply(manual, selected))))
        val manualPayload = mapper.mapPrice(null, 326, "", StartRegType.Default, null, listOf(manualDistance)).distances.single().member.single()
        assertNull(manualPayload.participantUserId); assertNull(manualPayload.userId)
    }

    @Test fun staleOrRevokedTeamAccessCannotProceedAndReloadDoesNotResetTheForm() {
        val selected = memberSelectApply(team, statement())
        val initial = RegistrationMemberStore.State(0, selected, listOf(team))
        assertFalse(initial.isSelectedTeamAvailable)
        with(RegistrationMemberReducer) {
            val loaded = initial.reduce(RegistrationMemberStore.Message.OnMembersChanged(listOf(team)))
            assertTrue(loaded.isSelectedTeamAvailable)
            val loading = loaded.reduce(RegistrationMemberStore.Message.MembersLoading)
            assertFalse(loading.isSelectedTeamAvailable)
            val failure = loading.reduce(RegistrationMemberStore.Message.MembersFailed("offline"))
            assertFalse(failure.isSelectedTeamAvailable); assertEquals(selected.copy(members = listOf(team)), failure.value)
            assertFalse(failure.reduce(RegistrationMemberStore.Message.OnMembersChanged(emptyList())).isSelectedTeamAvailable)
        }
    }

    private fun statement() = StartStatement(StartStatement.UserId.WithId("2310"), 491, "Иван", "Иванов", "01.01.2000", "26",
        "self@example.test", "Мужской", "Томск", "Лично", "79000000000", "", 0,
        cities = emptyList(), teams = emptyList(), sexList = emptyList(), members = emptyList(), paymentDisabled = false,
        yearDiscountApplied = false, distanceTitle = "Ретро кросс", participantUserId = 2310, participantSource = ProfileMember.Source.Self)
}
