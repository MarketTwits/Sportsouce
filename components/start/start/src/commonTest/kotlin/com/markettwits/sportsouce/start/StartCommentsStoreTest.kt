package com.markettwits.sportsouce.start

import app.cash.paging.PagingData
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.start.domain.*
import com.markettwits.sportsouce.start.presentation.comments.store.*
import com.markettwits.sportsouce.start.presentation.comments.store.StartCommentsStore.Intent
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult
import com.markettwits.sportsouce.start.presentation.start.component.toCommentData
import com.markettwits.sportsouce.start.presentation.start.component.CommentMode
import com.markettwits.sportsouce.start.presentation.start.component.CommentUiState
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class StartCommentsStoreTest {
    @BeforeTest fun setup() { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    @AfterTest fun cleanup() { Dispatchers.resetMain() }

    @Test fun failedSendKeepsDraftAndQuestionAndDuplicateTapsDoNotPostTwice() = runTest {
        val repo = FakeComments()
        val store = StartCommentsStoreFactory(DefaultStoreFactory(), repo).create()
        try {
            store.accept(Intent.ApplyStartId(318))
            store.accept(Intent.OnCommentChanged("  Вопрос  "))
            store.accept(Intent.OnQuestionChanged(true))
            store.accept(Intent.OnClickSendComment(store.state.comment))
            store.accept(Intent.OnClickSendComment(store.state.comment))
            assertTrue(store.state.isLoadingSendComment)
            assertEquals(1, repo.posts.size)
            assertEquals(Post(318, "Вопрос", 318, false, true), repo.posts.single())
            store.accept(Intent.OnCommentChanged("Не должен потеряться вопрос"))
            store.accept(Intent.OnQuestionChanged(false))
            repo.response.complete(CommentUiState.Error("Нет соединения")); runCurrent()
            assertFalse(store.state.isLoadingSendComment)
            assertEquals("  Вопрос  ", store.state.comment)
            assertTrue(store.state.isQuestion)
            assertEquals(0, store.state.sentCommentVersion)
            repo.response = CompletableDeferred()
            store.accept(Intent.OnClickSendComment(store.state.comment))
            repo.response.complete(CommentUiState.Success); runCurrent()
            assertEquals("", store.state.comment)
            assertFalse(store.state.isQuestion)
            assertEquals(1, store.state.sentCommentVersion)
            assertEquals(2, repo.loads)
        } finally { store.dispose() }
    }

    @Test fun repliesNeverSendQuestionFlagAndOrdinaryReplyDoesNotMarkQuestionAnswered() = runTest {
        val repo = FakeComments()
        val store = StartCommentsStoreFactory(DefaultStoreFactory(), repo).create(
            CommentMode.Reply("Имя", 1, repo.rows.single().toCommentData())
        )
        try {
            store.accept(Intent.ApplyStartId(318))
            store.accept(Intent.OnQuestionChanged(true))
            assertIs<CommentMode.Reply>(store.state.mode)
            store.accept(Intent.OnCommentChanged("Ответ"))
            store.accept(Intent.OnClickSendComment("Ответ"))
            assertEquals(Post(318, "Ответ", 1, true, false), repo.posts.single())
            repo.response.complete(CommentUiState.Success); runCurrent()
            assertFalse(store.state.comments.rows.single().isAnswered)
            assertIs<CommentMode.Reply>(store.state.mode)
            assertEquals("", store.state.comment)
        } finally { store.dispose() }
    }

    @Test fun filtersUseServerFlagsAndRefreshUpdatesUnansweredCountWithoutChangingFilter() = runTest {
        val repo = FakeComments()
        val store = StartCommentsStoreFactory(DefaultStoreFactory(), repo).create()
        try {
            repo.rows += row(2, question = true, answered = true)
            repo.rows += row(3, review = true)
            repo.rows += row(4)
            store.accept(Intent.ApplyStartId(318))
            assertEquals(1, store.state.unansweredCount)
            store.accept(Intent.OnFilterChanged(StartCommentsFilter.Unanswered))
            assertEquals(listOf(1), store.state.visibleComments.map { it.id })
            repo.rows[0] = row(1, question = true, answered = true)
            store.accept(Intent.OnRefresh); runCurrent()
            assertEquals(0, store.state.unansweredCount)
            assertTrue(store.state.visibleComments.isEmpty())
            assertEquals(StartCommentsFilter.Unanswered, store.state.filter)
            store.accept(Intent.OnFilterChanged(StartCommentsFilter.Reviews))
            assertEquals(listOf(3), store.state.visibleComments.map { it.id })
            store.accept(Intent.OnFilterChanged(StartCommentsFilter.Answered))
            assertEquals(listOf(1, 2), store.state.visibleComments.map { it.id })
        } finally { store.dispose() }
    }

    @Test fun blankDraftIsNotPostedAndRefreshFailureAfterSendDoesNotRestoreAlreadySentDraft() = runTest {
        val repo = FakeComments()
        val store = StartCommentsStoreFactory(DefaultStoreFactory(), repo).create()
        try {
            store.accept(Intent.ApplyStartId(318))
            store.accept(Intent.OnClickSendComment(" \n "))
            assertTrue(repo.posts.isEmpty())
            store.accept(Intent.OnCommentChanged("Комментарий"))
            store.accept(Intent.OnClickSendComment("Комментарий"))
            repo.failRead = true
            repo.response.complete(CommentUiState.Success); runCurrent()
            assertEquals("", store.state.comment)
            assertNotNull(store.state.error)
            assertEquals(1, store.state.comments.rows.size)
            assertFalse(store.state.isLoadingComments)
            assertFalse(store.state.isLoadingSendComment)
        } finally { store.dispose() }
    }

    private data class Post(val start: Int, val text: String, val parent: Int, val reply: Boolean, val question: Boolean)
    private class FakeComments : StartRepository {
        var response = CompletableDeferred<CommentUiState>()
        val posts = mutableListOf<Post>()
        val rows = mutableListOf(row(1, question = true))
        var loads = 0
        var failRead = false
        override suspend fun startComments(startId: Int): Result<StartItem.Comments> {
            loads++
            return if (failRead) Result.failure(IllegalStateException("offline")) else Result.success(StartItem.Comments(rows.size, rows.toList()))
        }
        override suspend fun writeComment(startId: Int, comment: String, id: Int, subComment: Boolean, isQuestion: Boolean): CommentUiState {
            posts += Post(startId, comment, id, subComment, isQuestion)
            return response.await()
        }
        override val favoritesFlow = MutableStateFlow<List<StartsListItem>>(emptyList())
        override val recentStartsFlow = MutableStateFlow<List<StartsListItem>>(emptyList())
        override suspend fun start(startId: String, relaunch: Boolean): Result<StartItem> = error("unused")
        override suspend fun refreshFavorites(): Result<List<StartsListItem>> = error("unused")
        override suspend fun startAddToFavorite(startItem: StartItem): Result<Boolean> = error("unused")
        override suspend fun startRemoveFromFavorites(startItem: StartItem): Result<Boolean> = error("unused")
        override suspend fun isStartInFavorite(startId: String): Result<Boolean> = error("unused")
        override suspend fun startMembersResult(startId: Int, maxResultCount: Int): List<MemberResult> = error("unused")
        override suspend fun startsRecommended(startId: String): Result<List<StartsListItem>> = error("unused")
        override suspend fun startsArchive(archiveId: Int): Result<List<StartsListItem>> = error("unused")
        override suspend fun startsSeries(seriesId: Int): Result<List<StartsListItem>> = error("unused")
        override suspend fun membersFilters(startId: Int): Result<FiltersRemote> = error("unused")
        override fun pagingMembers(startId: Int, params: StartMembersPagingParams): Flow<PagingData<Pair<StartMembersUi, Int>>> = error("unused")
        override fun pagingMembersResults(startId: Int, params: StartMembersResultsPagingParams): Flow<PagingData<Pair<MemberResult, Int>>> = error("unused")
    }
    companion object {
        fun row(id: Int, question: Boolean = false, answered: Boolean = false, review: Boolean = false) = StartItem.Comments.Row(
            id = id, comment = "Комментарий", countSub = 0, createdAt = "24 октября 2026", updatedAt = "24 октября 2026", personId = "1",
            replies = emptyList(), user = StartItem.Comments.User(1, "Имя", null, "Фамилия"),
            isQuestion = question, isAnswered = answered, isReview = review,
        )
    }
}
