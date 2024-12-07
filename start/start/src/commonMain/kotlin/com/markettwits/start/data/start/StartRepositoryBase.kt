package com.markettwits.start.data.start

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.cloud.api.TimeApi
import com.markettwits.cloud.exception.networkExceptionHandler
import com.markettwits.cloud.model.time.TimeRemote
import com.markettwits.core_ui.items.base.fetchFifth
import com.markettwits.core_ui.items.base_extensions.retryRunCatchingAsync
import com.markettwits.profile.api.AuthDataSource
import com.markettwits.start.data.start.mapper.start.StartRemoteToUiMapper
import com.markettwits.start.domain.StartItem
import com.markettwits.start.domain.StartRepository
import com.markettwits.start.presentation.start.component.CommentUiState
import com.markettwits.start_cloud.api.start.SportSauceStartApi
import com.markettwits.start_cloud.model.comments.request.StartCommentRequest
import com.markettwits.start_cloud.model.comments.request.StartSubCommentRequest
import com.markettwits.start_cloud.model.comments.response.Comment
import com.markettwits.start_cloud.model.members.StartMember
import com.markettwits.start_cloud.model.start.StartRemote
import com.markettwits.start_cloud.model.start.fields.album.StartAlbum

internal class StartRepositoryBase(
    private val service: SportsouceApi,
    private val startService : SportSauceStartApi,
    private val timeService: TimeApi,
    private val authService: AuthDataSource,
    private val mapper: StartRemoteToUiMapper,
    private val cache: StartMemoryCache
) : StartRepository {

    override suspend fun start(
        startId: Int,
        relaunch: Boolean
    ): Result<StartItem> {
        return if (relaunch)
            return launches(startId)
        else retryRunCatchingAsync(times = 2, interval = 2000L) {
            cache.get(startId)
        }.getOrNull() ?: launches(startId)
    }

    override suspend fun startComments(startId: Int): Result<StartItem.Comments> =
        retryRunCatchingAsync {
            val value = startService.comments(startId)
            mapper.map(value)
        }

    private suspend fun launches(startId: Int): Result<StartItem> {
        val result = runCatching {
            val cloud =
                fetchFifth<StartRemote, List<StartMember>, List<StartAlbum>, List<Comment>, TimeRemote>(
                    { startService.start(startId) },
                    { safeCallStartMembers(startId) },
                    { startService.albums(startId) },
                    { startService.comments(startId) },
                    { timeService.currentTime() },
                )
            val result =
                mapper.map(cloud.first, cloud.second, cloud.third, cloud.fourth, cloud.fifth)
            cache.set(value = Result.success(result), key = startId)
            result
        }
        return result
    }

    override suspend fun writeComment(
        comment: String,
        id: Int,
        subComment: Boolean
    ): CommentUiState {
        return try {
            val token = authService.updateToken().getOrThrow()
            val userId = authService.auth().getOrThrow().id
            if (subComment)
                startService.writeSubComment(
                    subComment = StartSubCommentRequest(
                        comment = comment,
                        parentCommentId = id,
                        personId = userId.toString()
                    ),
                    token = token
                )
            else
                startService.writeComment(
                    startCommentRequest = StartCommentRequest(
                        comment = comment,
                        startId = id,
                        personId = userId.toString()
                    ),
                    token = token
                )
            CommentUiState.Success
        } catch (e: Exception) {
            CommentUiState.Error(networkExceptionHandler(e).message.toString())
        }
    }

    private suspend fun safeCallStartMembers(startId: Int) : List<StartMember> {
        return kotlin.runCatching {
            startService.membersNew(startId)
        }.fold(onSuccess = {it}, onFailure = { emptyList() })
    }
}