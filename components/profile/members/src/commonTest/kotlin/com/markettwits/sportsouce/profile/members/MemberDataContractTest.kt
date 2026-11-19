package com.markettwits.sportsouce.profile.members

import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.sportsouce.profile.cloud.model.members.ProfileMembers
import com.markettwits.sportsouce.profile.cloud.model.identity.IdentitySubject
import com.markettwits.sportsouce.profile.members.member_common.data.registrationMember
import com.markettwits.sportsouce.profile.members.member_add_edit.domain.validate.AddOrEditMemberValidatorBase
import com.markettwits.sportsouce.profile.members.member_common.data.mapper.MembersMapperBase
import com.markettwits.sportsouce.profile.members.member_common.domain.*
import kotlinx.serialization.json.Json
import kotlin.test.*

class MemberDataContractTest {
    @Test fun teamRegistrationKeepsTheApiCalendarDateAndFullProfile() {
        val person = IdentitySubject(1775, "Дмитрий", "Сергеев", "Иванович", "1972-01-17T17:00:00.000Z",
            1972, "Мужской", "Новосибирск", "СпортСоюз", "runner@example.test", "79000000001")
        val member = person.registrationMember("coach")!!
        assertEquals("17.01.1972", member.birthday)
        assertEquals(1775, member.participantUserId); assertEquals(0, member.userId)
        assertEquals("Новосибирск", member.city); assertEquals("Иванович", member.patronymic)
        assertEquals("79000000001", member.phone); assertEquals("runner@example.test", member.email)
        assertEquals("Тренер", member.type); assertEquals(ProfileMember.Source.Team, member.source)
        assertNull(IdentitySubject(1775).registrationMember(null))
        val incomplete = person.copy(birthday = null, phone = null, email = null).registrationMember(null)!!
        assertEquals("", incomplete.birthday); assertEquals("", incomplete.phone)
    }

    private val mapper = MembersMapperBase(BaseTimeMapper())
    private val validator = AddOrEditMemberValidatorBase()
    private val json = Json { ignoreUnknownKeys = true }
    private fun member() = emptyProfileMember.copy(id = 2957, name = "Константин", surname = "Маняшин",
        birthday = "01.12.2005", type = "Тренер", gender = "Мужской", team = "Лично")

    @Test fun oldTimestampsUseTheWebTimezoneButDateOnlyBirthdaysDoNotShift() {
        val source = listOf("2005-12-01", "1986-03-02T17:00:00.000Z", "2004-01-25T18:00:00.000Z")
        val expected = listOf("01.12.2005", "03.03.1986", "26.01.2004")
        for ((date, preview) in source.zip(expected)) {
            val item = mapper.mapMember(ProfileMembers.ProfileMember(2957, birthday = date, type = null, child = null), ownerId = 93)
            assertEquals(preview, item.birthday)
            assertEquals(93, item.userId); assertFalse(item.child); assertEquals("", item.type)
        }
        val nullDate = mapper.mapMember(ProfileMembers.ProfileMember(1), 93)
        assertEquals("", nullDate.birthday)
    }

    @Test fun requestUsesCalendarDateDigitsAndOnlyCreationSuppliesOwner() {
        val member = member().copy(name = " Константин ", surname = " Маняшин ", phone = "+7 (900) 000-00-00", email = " runner@example.test ")
        val update = mapper.addOrUpdate(member, update = true, userId = 2310)
        assertEquals("2005-12-01", update.birthday); assertEquals("79000000000", update.phone)
        assertEquals("runner@example.test", update.email); assertEquals("Константин", update.name)
        assertNull(update.id); assertNull(update.userId)
        assertEquals("2310", mapper.addOrUpdate(member, update = false, userId = 2310).userId)
        assertEquals("79000000000", mapper.addOrUpdate(member.copy(phone = "89000000000"), true, 2310).phone)
        assertEquals("79000000000", mapper.addOrUpdate(member.copy(phone = "9000000000"), true, 2310).phone)
    }

    @Test fun childFollowsTheWebPayloadAndContactsAreOptionalForAdults() {
        for (type in listOf("Тренер", "Ученик", "Родственник", "Друг", "Партнер по команде")) {
            assertTrue(validator.validateFields(member().copy(type = type)).isSuccess)
            assertTrue(type in MemberRelationship.options)
        }
        val child = member().copy(child = true, phone = "old value", email = "old value")
        assertTrue(validator.validateFields(child).isSuccess)
        val request = mapper.addOrUpdate(child, true, 2310)
        assertEquals("", request.phone); assertNull(request.email)
        assertTrue(validator.validateFields(member().copy(phone = "123")).isFailure)
        assertTrue(validator.validateFields(member().copy(email = "wrong-address")).isFailure)
        assertTrue(validator.validateFields(member().copy(type = " ")).isFailure)
        assertTrue(validator.validateFields(member().copy(team = " ")).isFailure)
        assertTrue(validator.validateFields(member().copy(birthday = "31.02.2005")).isFailure)
        assertTrue(validator.validateFields(member().copy(birthday = "01.12.2999")).isFailure)
    }

    @Test fun identityFieldsSurviveMappingAndLegacyCachesRemainReadable() {
        val source = ProfileMembers.ProfileMember(7, participantUserId = 1282, cityId = 15, telegram = "@runner", vk = "https://vk.com/runner")
        val mapped = mapper.mapMember(source, 93)
        assertTrue(mapped.identityKnown); assertEquals(1282, mapped.participantUserId)
        assertEquals(15, mapped.cityId); assertEquals("@runner", mapped.telegram)
        val old = json.decodeFromString<ProfileMember>("""{"id":7,"userId":93,"name":"Иван","surname":"Иванов","email":"","phone":"","gender":"Мужской","team":"","birthday":"01.12.2005","type":"Друг","child":false}""")
        assertFalse(old.identityKnown); assertNull(old.participantUserId)
        assertEquals(listOf("Новая связь"), MemberRelationship.optionsFor("Новая связь") - MemberRelationship.options.toSet())
    }
}
