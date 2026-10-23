package com.markettwits.sportsouce.profile

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.markettwits.sportsouce.edit_profile.sizes.domain.*
import com.markettwits.sportsouce.edit_profile.sizes.presentation.store.*
import com.markettwits.sportsouce.edit_profile.sizes.presentation.store.ProfileSizesStore.Intent
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileSizesStoreTest {
    @BeforeTest fun main() { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    @AfterTest fun resetMain() { Dispatchers.resetMain() }

    @Test fun optionalValuesDecimalCommaAndWebExamplesAreAcceptedWithoutGuessingSizeSystems() {
        assertTrue(ProfileSizes().errors().isEmpty())
        assertTrue(ProfileSizes(clothingSize = "12", footLengthMm = "23").errors().isEmpty())
        val sizes = ProfileSizes(" M / 48 ", "265", "42,5", "9.5", "27")
        assertTrue(sizes.errors().isEmpty())
        assertEquals("42.5", sizes.normalized().shoeSizeRu)
        assertEquals("M / 48", sizes.normalized().clothingSize)
        val errors = ProfileSizes("x".repeat(21), "-1", "abc", "0", "1.2.3").errors()
        assertEquals(ProfileSizeField.entries.toSet(), errors.keys)
    }

    @Test fun failedSaveKeepsDraftRetryWorksAndDoubleSubmissionIsBlocked() = runTest {
        val repo = FakeRepository()
        val store = ProfileSizesStoreFactory(DefaultStoreFactory(), repo).create()
        try {
            runCurrent()
            assertEquals(repo.loaded, store.state.data)
            store.accept(Intent.Edit(ProfileSizeField.ShoeUs, "9,5"))
            repo.pending = CompletableDeferred()
            store.accept(Intent.Save); store.accept(Intent.Save)
            runCurrent()
            assertTrue(store.state.isSaving)
            assertEquals(1, repo.calls.size)
            assertEquals("9.5", repo.calls.single().shoeSizeUs)
            store.accept(Intent.Edit(ProfileSizeField.ShoeUs, "12"))
            assertEquals("9,5", store.state.data?.shoeSizeUs)
            repo.pending!!.complete(Result.failure(IllegalStateException("offline"))); runCurrent()
            assertFalse(store.state.isSaving)
            assertTrue(store.state.hasChanges)
            assertEquals("9,5", store.state.data?.shoeSizeUs)
            repo.pending = null
            store.accept(Intent.Save); runCurrent()
            assertEquals(2, repo.calls.size)
            assertEquals("9.5", store.state.data?.shoeSizeUs)
            assertFalse(store.state.hasChanges)
        } finally { store.dispose() }
    }

    @Test fun invalidFieldsPreventRequestAndClearedFieldsAreSavedAndBackConfirmsOnlyDirtyData() = runTest {
        val repo = FakeRepository()
        val store = ProfileSizesStoreFactory(DefaultStoreFactory(), repo).create()
        val labels = mutableListOf<ProfileSizesStore.Label>()
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { store.labels.collect { labels += it } }
        try {
            runCurrent()
            store.accept(Intent.Edit(ProfileSizeField.FootLength, "не число")); store.accept(Intent.Save)
            assertEquals(setOf(ProfileSizeField.FootLength), store.state.errors.keys)
            assertTrue(repo.calls.isEmpty())
            store.accept(Intent.Edit(ProfileSizeField.FootLength, ""))
            assertTrue(store.state.errors.isEmpty())
            store.accept(Intent.GoBack)
            assertTrue(store.state.confirmDiscard); assertTrue(labels.isEmpty())
            store.accept(Intent.KeepEditing); store.accept(Intent.Reset)
            assertEquals(repo.loaded, store.state.data); assertFalse(store.state.hasChanges)
            ProfileSizeField.entries.forEach { store.accept(Intent.Edit(it, "")) }
            store.accept(Intent.Save); runCurrent()
            assertEquals(ProfileSizes(), repo.calls.single())
            assertEquals(ProfileSizes(), store.state.data)
            store.accept(Intent.GoBack)
            assertEquals(listOf<ProfileSizesStore.Label>(ProfileSizesStore.Label.GoBack), labels)
        } finally { job.cancel(); store.dispose() }
    }

    @Test fun retryRecoversLoadingErrorWithoutInventingBlankSavedValues() = runTest {
        val repo = FakeRepository().apply { failLoad = true }
        val store = ProfileSizesStoreFactory(DefaultStoreFactory(), repo).create()
        try {
            runCurrent()
            assertNotNull(store.state.loadError); assertNull(store.state.data)
            store.accept(Intent.Save); assertTrue(repo.calls.isEmpty())
            repo.failLoad = false
            store.accept(Intent.Retry); runCurrent()
            assertEquals(repo.loaded, store.state.data); assertNull(store.state.loadError)
        } finally { store.dispose() }
    }

    private class FakeRepository : ProfileSizesRepository {
        val loaded = ProfileSizes("M", "265", "42", "9", "27")
        var failLoad = false
        var pending: CompletableDeferred<Result<ProfileSizes>>? = null
        val calls = mutableListOf<ProfileSizes>()
        override suspend fun fetch() = if (failLoad) Result.failure(IllegalStateException("offline")) else Result.success(loaded)
        override suspend fun save(sizes: ProfileSizes): Result<ProfileSizes> {
            calls += sizes
            return pending?.await() ?: Result.success(sizes)
        }
    }
}
