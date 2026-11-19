package com.markettwits.sportsouce.profile.members

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.profile.members.member_add_edit.domain.add.MemberAddUseCase
import com.markettwits.sportsouce.profile.members.member_add_edit.domain.edit.MemberEditUseCase
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.component.MemberEditComponent
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.store.*
import com.markettwits.sportsouce.profile.members.member_common.domain.*
import com.markettwits.sportsouce.teams_city.domain.Team
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class MemberEditBehaviorTest {
    @Test fun slowTeamDirectoryDoesNotBlockEditingOrResetTheDraft() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        try {
            val directory = CompletableDeferred<Result<List<Team>>>()
            val fresh = emptyProfileMember.copy(id = 7, name = "Иван", team = "Лично")
            val useCase = object : MemberEditUseCase {
                override suspend fun member(memberId: Int) = Result.success(fresh)
                override suspend fun teams() = directory.await()
                override suspend fun edit(profileMember: ProfileMember) = Result.success(Unit)
            }
            val add = object : MemberAddUseCase { override suspend fun addMember(member: ProfileMember) = Result.success(Unit) }
            val store = MemberEditStoreFactory(DefaultStoreFactory(), useCase, add).create(fresh, MemberEditComponent.Mode.Edit)
            assertFalse(store.state.isLoading)
            assertTrue(store.state.teamsLoading)
            store.accept(MemberEditStore.Intent.OnValueChanged(fresh.copy(name = "Пётр")))
            directory.complete(Result.failure(IllegalStateException("directory unavailable")))
            assertEquals("Пётр", store.state.member.name)
            assertTrue(store.state.teamsError)
            assertFalse(store.state.teamsLoading)
            assertNull(store.state.loadError)
            store.accept(MemberEditStore.Intent.RetryTeams)
            assertEquals("Пётр", store.state.member.name)
            store.dispose()
        } finally { Dispatchers.resetMain() }
    }

    @Test fun editorLoadsCurrentDetailAndDirectoryFailureDoesNotBlockSaving() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        try {
            val gate = CompletableDeferred<Result<Unit>>()
            var writes = 0
            val fresh = emptyProfileMember.copy(id = 7, name = "Иван", type = "Тренер", team = "Лично")
            val useCase = object : MemberEditUseCase {
                override suspend fun member(memberId: Int) = Result.success(fresh)
                override suspend fun teams(): Result<List<Team>> = Result.failure(IllegalStateException("directory unavailable"))
                override suspend fun edit(profileMember: ProfileMember): Result<Unit> { writes++; return gate.await() }
            }
            val add = object : MemberAddUseCase { override suspend fun addMember(member: ProfileMember) = Result.success(Unit) }
            val store = MemberEditStoreFactory(DefaultStoreFactory(), useCase, add).create(fresh.copy(type = "Друг"), MemberEditComponent.Mode.Edit)
            assertEquals("Тренер", store.state.member.type); assertFalse(store.state.isLoading)
            store.accept(MemberEditStore.Intent.Save); store.accept(MemberEditStore.Intent.Save)
            assertEquals(1, writes); assertTrue(store.state.isSaving); assertFalse(store.state.isLoading)
            store.accept(MemberEditStore.Intent.OnValueChanged(fresh.copy(name = "Другое")))
            assertEquals("Иван", store.state.member.name)
            gate.complete(Result.failure(IllegalStateException("save failed")))
            assertFalse(store.state.isSaving); assertNull(store.state.loadError)
            assertEquals("Иван", store.state.member.name)
            store.dispose()
        } finally { Dispatchers.resetMain() }
    }
}
