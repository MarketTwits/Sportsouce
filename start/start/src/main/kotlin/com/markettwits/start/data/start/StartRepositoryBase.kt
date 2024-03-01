package com.markettwits.start.data.start

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.cloud.api.TimeApi
import com.markettwits.cloud.model.auth.common.AuthErrorResponse
import com.markettwits.cloud.model.auth.common.AuthException
import com.markettwits.cloud.model.start_comments.request.StartCommentRequest
import com.markettwits.cloud.model.start_comments.request.StartSubCommentRequest
import com.markettwits.core_ui.base.Fourth
import com.markettwits.core_ui.base_extensions.retryRunCatchingAsync
import com.markettwits.profile.data.AuthDataSource
import com.markettwits.start.data.start.mapper.StartRemoteToUiMapper
import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.start.component.CommentUiState
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class StartRepositoryBase(
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
            return launch(startId)
        else retryRunCatchingAsync(times = 2, interval = 2000L) {
            cache.get(startId)
        }.getOrNull() ?: launch(startId)
    }

    override suspend fun startComments(startId: Int): Result<StartItem.Comments> =
        retryRunCatchingAsync {
            val value = service.fetchStartComments(startId)
            mapper.map(value)
        }

    private suspend fun launch(startId: Int): Result<StartItem> {
        return retryRunCatchingAsync {
            val (data, withFilter, comments, time) = coroutineScope {
                withContext(Dispatchers.IO) {
                    val deferredTime = async { timeService.currentTime() }
                    val deferredData = async { service.fetchStart(startId) }
                    val deferredWithFilter = async { service.fetchStartMember(startId) }
                    val deferredComments = async { service.fetchStartComments(startId) }
                    Fourth(
                        deferredData.await(),
                        deferredWithFilter.await(),
                        deferredComments.await(),
                        deferredTime.await()
                    )
                }
            }
            val result = mapper.map(data, withFilter, comments, time)
            cache.set(value = Result.success(result), key = startId)
            result
        }
    }


    override suspend fun writeComment(comment: String, startId: Int): CommentUiState {
        return try {
            val token = authService.updateToken()
            val user = authService.auth()
            service.writeComment(
                startCommentRequest = StartCommentRequest(
                    comment,
                    startId,
                    user.getOrThrow().id.toString()
                ),
                token = token.getOrThrow()
            )
            CommentUiState.Success
        } catch (e: Exception) {
            when (e) {
                is AuthException -> CommentUiState.Error(e.exception)
                is java.net.UnknownHostException -> CommentUiState.Error("Нет интернета")
                is HttpRequestTimeoutException -> CommentUiState.Error("Неустойчевое интернет соединение")
                is ResponseException -> CommentUiState.Error(e.response.body<AuthErrorResponse>().message)
                else -> CommentUiState.Error(e.message.toString())
            }
        }
    }

    override suspend fun writeSubComment(comment: String, parentCommentId: Int): CommentUiState {
        return try {
            val token = authService.updateToken()
            val user = authService.auth()
            service.writeSubComment(
                subComment = StartSubCommentRequest(
                    comment,
                    parentCommentId,
                    user.getOrThrow().id.toString()
                ),
                token = token.getOrThrow()
            )
            CommentUiState.Success
        } catch (e: Exception) {
            when (e) {
                is AuthException -> CommentUiState.Error(e.exception)
                is ClientRequestException -> CommentUiState.Error(e.response.body<AuthErrorResponse>().message)
                else -> CommentUiState.Error(e.message.toString())
            }
        }
    }
}