package com.markettwits.sportsouce.start.register

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.profile.members.member_common.data.ProfileMembersRepository
import com.markettwits.sportsouce.profile.members.member_common.domain.*
import com.markettwits.sportsouce.start.register.domain.StartStatement
import com.markettwits.sportsouce.start.register.presentation.registration.member.components.memberSelectApply
import com.markettwits.sportsouce.start.register.presentation.registration.member.domain.*
import com.markettwits.sportsouce.start.register.presentation.registration.member.store.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class TeamRegistrationFlowTest {
    private val person = emptyProfileMember.copy(name = "Дмитрий", surname = "Сергеев", birthday = "17.01.1972", gender = "Мужской",
        team = "СпортСоюз", city = "Новосибирск", email = "runner@example.test", phone = "79000000001", participantUserId = 1775,
        source = ProfileMember.Source.Team)
    private val self = person.copy(id = 2310, userId = 2310, participantUserId = 2310, source = ProfileMember.Source.Self, name = "Иван")
    private val saved = person.copy(id = 2957, userId = 2310, participantUserId = null, source = ProfileMember.Source.Saved)

    @Test fun actualStoreLoadsOnlyMyTeamAndSelectionSurvivesInTheStatement() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        try {
            val identity = Identity()
            val profiles = Profiles()
            val loader = RegistrationParticipantsRepository(profiles, identity)
            val loaded = loader.load().getOrThrow()
            assertEquals(listOf(self, person, saved), loaded)
            val store = RegistrationMemberStoreFactory(DefaultStoreFactory(), RegistrationMemberValidatorBase(), profiles, loader)
                .create(0, statement(), listOf(self, saved))
            assertTrue(store.state.membersLoaded)
            store.accept(RegistrationMemberStore.Intent.SelectMember(store.state.members[1]))
            assertEquals(1775, store.state.value.participantUserId)
            assertEquals("Новосибирск", store.state.value.city)
            assertTrue(store.state.value.getSaveAge() > 50)
            assertTrue(store.state.isSelectedTeamAvailable)
            store.accept(RegistrationMemberStore.Intent.OnClickContinue)
            assertFalse(store.state.isSuggestAddDialogVisible)
            store.accept(RegistrationMemberStore.Intent.SelectMember(saved))
            assertNull(store.state.value.participantUserId)
            assertEquals(StartStatement.UserId.WithoutId, store.state.value.userId)
            store.dispose()
        } finally { Dispatchers.resetMain() }
    }

    @Test fun aFailedPermissionRefreshBlocksStaleTeamSelectionAndKeepsEnteredValues() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        try {
            val identity = Identity().apply { result = Result.failure(IllegalStateException("offline")) }
            val profiles = Profiles()
            val initial = memberSelectApply(person, statement())
            val store = RegistrationMemberStoreFactory(DefaultStoreFactory(), RegistrationMemberValidatorBase(), profiles,
                RegistrationParticipantsRepository(profiles, identity)).create(0, initial, listOf(person, self))
            assertNotNull(store.state.membersError)
            assertFalse(store.state.isSelectedTeamAvailable)
            assertEquals(initial, store.state.value)
            store.accept(RegistrationMemberStore.Intent.OnClickContinue)
            assertFalse(store.state.isSuggestAddDialogVisible)
            identity.result = Result.success(MemberIdentityTeam(myTeam = listOf(MemberTeamAccess(4, 1775, "Дмитрий", null, false, person))))
            store.accept(RegistrationMemberStore.Intent.RetryMembers)
            assertNull(store.state.membersError)
            assertTrue(store.state.isSelectedTeamAvailable)
            store.dispose()
        } finally { Dispatchers.resetMain() }
    }

    private inner class Profiles : ProfileMembersRepository {
        override suspend fun fetchMembers(withUser: Boolean) = listOf(self, saved).also { assertTrue(withUser) }
        override suspend fun observeMembers(forced: Boolean, withUser: Boolean): Flow<List<ProfileMember>> = error("Unexpected cache access")
        override suspend fun memberDetail(memberId: Int): Result<ProfileMember> = error("Unexpected detail")
        override suspend fun deleteMember(memberId: Int): Result<Unit> = error("Unexpected mutation")
        override suspend fun updateMember(profileMember: ProfileMember): Result<Unit> = error("Unexpected mutation")
        override suspend fun addMember(profileMember: ProfileMember): Result<Unit> = error("Unexpected mutation")
        override suspend fun userAsMember() = Result.success(self)
    }
    private inner class Identity : MemberIdentityRepository {
        var result = Result.success(MemberIdentityTeam(
            myTeam = listOf(MemberTeamAccess(4, 1775, "Дмитрий", null, false, person)),
            trusted = listOf(MemberTeamAccess(8, 92, "Другой", null, true, person.copy(participantUserId = 92))),
            outgoing = listOf(MemberConnectionRequest(5, 1282, "pending"))))
        override suspend fun team() = result
        override suspend fun search(name: String, surname: String, birthYear: Int, profileId: Int?): Result<MemberSearchResult> = error("Unexpected search")
        override suspend fun requestConnection(userId: Int): Result<MemberConnectionRequest> = error("Unexpected mutation")
        override suspend fun respondToRequest(requestId: Int, accept: Boolean): Result<Unit> = error("Unexpected mutation")
        override suspend fun updateRelation(userId: Int, relation: String?): Result<Unit> = error("Unexpected mutation")
        override suspend fun updateAutoAccept(trustId: Int, enabled: Boolean): Result<Unit> = error("Unexpected mutation")
        override suspend fun addTrustedPerson(userId: Int): Result<Unit> = error("Unexpected mutation")
    }
    private fun statement() = StartStatement(StartStatement.UserId.WithId("2310"), 491, "Иван", "Иванов", "01.01.2000", "26",
        "self@example.test", "Мужской", "Томск", "Лично", "79000000000", "", 0,
        cities = emptyList(), teams = emptyList(), sexList = emptyList(), members = emptyList(), paymentDisabled = false,
        yearDiscountApplied = false, distanceTitle = "Ретро кросс", participantUserId = 2310, participantSource = ProfileMember.Source.Self)
}
