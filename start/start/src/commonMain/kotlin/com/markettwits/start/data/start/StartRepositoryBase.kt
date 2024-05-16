package com.markettwits.start.data.start

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.cloud.api.TimeApi
import com.markettwits.cloud.exception.networkExceptionHandler
import com.markettwits.cloud.model.start.StartRemote
import com.markettwits.cloud.model.start_album.StartAlbumRemote
import com.markettwits.cloud.model.start_comments.request.StartCommentRequest
import com.markettwits.cloud.model.start_comments.request.StartSubCommentRequest
import com.markettwits.cloud.model.start_comments.response.StartCommentsRemote
import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.cloud.model.time.TimeRemote
import com.markettwits.core_ui.items.base.fetchFifth
import com.markettwits.core_ui.items.base_extensions.retryRunCatchingAsync
import com.markettwits.profile.api.AuthDataSource
import com.markettwits.start.data.start.mapper.start.StartRemoteToUiMapper
import com.markettwits.start.domain.StartItem
import com.markettwits.start.domain.StartRepository
import com.markettwits.start.presentation.start.component.CommentUiState

internal class StartRepositoryBase(
    private val service: SportsouceApi,
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
            val value = service.fetchStartComments(startId)
            mapper.map(value)
        }

    private suspend fun launches(startId: Int): Result<StartItem> {
        val result = runCatching {
            val cloud =
                fetchFifth<StartRemote, List<StartMemberItem>, StartAlbumRemote, StartCommentsRemote, TimeRemote>(
                    { service.fetchStart(startId) },
                    { service.fetchStartMember(startId) },
                    { service.fetchStartAlbum(startId) },
                    { service.fetchStartComments(startId) },
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
                service.writeSubComment(
                    subComment = StartSubCommentRequest(comment, id, userId.toString()),
                    token = token
                )
            else
                service.writeComment(
                    startCommentRequest = StartCommentRequest(comment, id, userId.toString()),
                    token = token
                )
            CommentUiState.Success
        } catch (e: Exception) {
            CommentUiState.Error(networkExceptionHandler(e).message.toString())
        }
    }
}