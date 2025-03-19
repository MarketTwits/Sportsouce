package com.markettwits.start.data.start

import com.markettwits.core.errors.api.throwable.networkExceptionHandler
import com.markettwits.core.log.LogTagProvider
import com.markettwits.core.log.errorLog
import com.markettwits.core_ui.items.extensions.fetchFifth
import com.markettwits.core_ui.items.extensions.retryRunCatchingAsync
import com.markettwits.profile.api.AuthDataSource
import com.markettwits.start.data.start.mapper.start.StartRemoteToUiMapper
import com.markettwits.start.domain.StartItem
import com.markettwits.start.domain.StartRepository
import com.markettwits.start.presentation.result.model.MemberResult
import com.markettwits.start.presentation.start.component.CommentUiState
import com.markettwits.start_cloud.api.start.SportSauceStartApi
import com.markettwits.start_cloud.model.comments.request.StartCommentRequest
import com.markettwits.start_cloud.model.comments.request.StartSubCommentRequest
import com.markettwits.start_cloud.model.comments.response.Comment
import com.markettwits.start_cloud.model.members.StartMember
import com.markettwits.start_cloud.model.result.StartMemberResult
import com.markettwits.start_cloud.model.start.StartRemote
import com.markettwits.start_cloud.model.start.fields.album.StartAlbum
import com.markettwits.starts_common.domain.SportSauceStartsApi
import com.markettwits.starts_common.domain.StartsListItem

internal class StartRepositoryBase(
    private val startService: SportSauceStartApi,
    private val authService: AuthDataSource,
    private val startMapper: StartRemoteToUiMapper,
    private val startsService: SportSauceStartsApi,
    private val cache: StartMemoryCache
) : StartRepository, LogTagProvider {

    override val tag: String = "StartRepository"

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

    override suspend fun startMemberResults(
        startId: Int,
        query: String,
        limit: Int,
        offset: Int
    ): Result<List<MemberResult>> =
        kotlin.runCatching {
            startService.membersResults(1000, startId, query)
        }.map {
            startMapper.map(it)
        }

    override suspend fun startComments(startId: Int): Result<StartItem.Comments> =
        retryRunCatchingAsync {
            val value = startService.comments(startId)
            startMapper.map(value)
        }

    override suspend fun startsRecommended(startId: Int): Result<List<StartsListItem>> =
        runCatching {
            startsService.fetchActualStarts()
                .filter { it.id != startId }
                .shuffled()
        }

    private suspend fun launches(startId: Int): Result<StartItem> {
        val result = runCatching {
            val cloud =
                fetchFifth<StartRemote, List<StartMember>, List<StartAlbum>, List<Comment>, List<StartMemberResult>>(
                    { startService.start(startId) },
                    { safeCallStartMembers(startId) },
                    { startService.albums(startId) },
                    { startService.comments(startId) },
                    { safeCallStartMembersResults(startId) }
                )
            val result =
                startMapper.map(cloud.first, cloud.second, cloud.fifth, cloud.third, cloud.fourth)
            cache.set(value = Result.success(result), key = startId)
            result
        }
        return result
    }

    override suspend fun writeComment(
        startId: Int,
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
                        personId = userId.toString(),
                        startId = startId
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
            CommentUiState.Error(e.networkExceptionHandler().message.toString())
        }
    }

    private suspend fun safeCallStartMembers(startId: Int): List<StartMember> {
        return kotlin.runCatching {
            startService.members(startId)
        }.fold(onSuccess = { it }
        ) {
            errorLog(it) { "Fail to launch startMembers start id $startId" }
            emptyList()
        }
    }

    private suspend fun safeCallStartMembersResults(startId: Int): List<StartMemberResult> {
        return kotlin.runCatching {
            startService.membersResults(1000, startId)
        }.fold(onSuccess = { it }, onFailure = {
            println(it)
            emptyList()
        })
    }
}